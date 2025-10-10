package top.teek.ag.system.service.impl;

import top.teek.ag.core.enums.BelongType;
import top.teek.ag.core.enums.TeamMemberRole;
import top.teek.ag.core.helper.AgHelper;
import top.teek.ag.system.mapper.TeamMapper;
import top.teek.ag.system.model.dto.TeamDTO;
import top.teek.ag.system.model.dto.TeamMemberDTO;
import top.teek.ag.system.model.po.Team;
import top.teek.ag.system.model.vo.TeamRouteVO;
import top.teek.ag.system.model.vo.router.Meta;
import top.teek.ag.system.model.vo.router.RouterVO;
import top.teek.ag.system.service.ProjectService;
import top.teek.ag.system.service.TeamMemberService;
import top.teek.ag.system.service.TeamService;
import top.teek.core.exception.ServiceException;
import top.teek.security.domain.LoginUser;
import top.teek.utils.MapstructUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Teeker
 * @date 2024-06-22 00:15:48
 * @note 针对表「t_team（团队表）」的数据库操作 Service 实现
 */
@Service
@RequiredArgsConstructor
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

    private final TeamMemberService teamMemberService;
    private final ProjectService projectService;

    @Override
    public List<RouterVO> listMyAllTeamRoute() {
        // 模拟用户
        String userId = AgHelper.getUserId();
        List<TeamRouteVO> teamRouteVOList = baseMapper.listMyAllTeam(userId);

        // 构建一级菜单
        RouterVO oneRouterVO = new RouterVO()
                .setPath("/team")
                .setName("Team")
                .setMeta(new Meta()
                        .setTitle("团队管理")
                        .setIcon("tdesign:user-circle")
                        .setAlwaysShowRoot(true)
                );

        // 构建二级菜单
        List<RouterVO> routerVOList = new ArrayList<>();

        teamRouteVOList.forEach(item -> {
            String teamRoleLabel = "";
            Integer teamRoleValue = item.getTeamRole();
            for (TeamMemberRole teamMemberRole : TeamMemberRole.values()) {
                if (teamMemberRole.ordinal() == teamRoleValue) {
                    teamRoleLabel = teamMemberRole.getLabel();
                }
            }
            String belong = item.getBelongType() == BelongType.CREATE.ordinal() ? "创建" : "加入";

            RouterVO routerVO = new RouterVO()
                    .setPath(item.getTeamId())
                    .setName(item.getTeamName())
                    .setComponent("/team/index")
                    .setMeta(new Meta()
                            .setTitle(item.getTeamName() + " (" + belong + ")")
                            .setHideInTab(true)
                            .setParams(Map.of(
                                    "id", item.getId(),
                                    "teamId", item.getTeamId(),
                                    "teamName", item.getTeamName(),
                                    "teamRole", teamRoleLabel
                            ))
                    );

            routerVOList.add(routerVO);
        });

        oneRouterVO.setChildren(routerVOList);
        return List.of(oneRouterVO);
    }

    @Override
    public List<TeamRouteVO> listMyAll() {
        return baseMapper.listMyAllTeam(AgHelper.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addTeam(TeamDTO teamDTO) {
        LoginUser loginUser = AgHelper.getLoginUser();
        if (Objects.isNull(loginUser)) {
            throw new ServiceException("用户未登录");
        }
        Team team = MapstructUtil.convert(teamDTO, Team.class);
        team.setOwnerId(loginUser.getUserId());
        team.setOwnerName(loginUser.getUsername());
        // ID 为自增，因此不需要设置
        team.setId(null);
        // 新增团队
        int result = baseMapper.insert(team);

        // 新增团队成员
        TeamMemberDTO teamMemberDTO = new TeamMemberDTO();
        teamMemberDTO.setTeamRole(TeamMemberRole.OWNER.ordinal())
                .setUserId(loginUser.getUserId())
                .setUsername(loginUser.getUsername())
                .setTeamId(team.getTeamId())
                .setBelongType(BelongType.CREATE.ordinal());

        teamMemberService.addTeamMembers(List.of(teamMemberDTO), null);

        return result > 0;
    }

    @Override
    public Boolean editTeam(TeamDTO teamDTO) {

        Team team = MapstructUtil.convert(teamDTO, Team.class);
        return baseMapper.updateById(team) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeTeam(String teamId) {
        // 删除所有的团队成员
        teamMemberService.removeAllTeamMember(teamId);
        // 删除所有项目
        projectService.removeAllProject(teamId);
        // 删除团队信息
        return baseMapper.delete(Wrappers.<Team>lambdaQuery()
                .eq(Team::getTeamId, teamId)) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean transferOwner(String teamId, String userId, String username) {
        if (Objects.equals(userId, AgHelper.getUserId())) {
            throw new ServiceException("不允许操作自己");
        }

        if (!teamMemberService.checkMemberExist(teamId, userId)) {
            throw new ServiceException("移交用户不在团队中");
        }

        Team team = new Team();
        team.setTeamId(teamId);
        team.setOwnerId(userId);
        team.setOwnerName(username);
        // 修改团队负责人
        int result = baseMapper.update(team, Wrappers.<Team>lambdaUpdate()
                .eq(Team::getTeamId, teamId)
                .set(Team::getOwnerId, userId));

        // 修改自己为普通成员
        teamMemberService.editTeamMemberRole(teamId, AgHelper.getUserId(), TeamMemberRole.MEMBER.ordinal());

        // 修改团队成员的角色为所有者
        teamMemberService.editTeamMemberRole(teamId, userId, TeamMemberRole.OWNER.ordinal());

        return result > 0;
    }

    @Override
    public boolean checkTeamNameUnique(TeamDTO teamDTO) {
        return baseMapper.exists(Wrappers.<Team>lambdaQuery()
                .eq(Team::getTeamName, teamDTO.getTeamName())
                .ne(Objects.nonNull(teamDTO.getId()), Team::getId, teamDTO.getId()));
    }
}




