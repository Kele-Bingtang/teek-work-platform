package top.teek.ag.system.service.impl;

import top.teek.ag.core.helper.AgHelper;
import top.teek.ag.system.mapper.TeamMemberMapper;
import top.teek.ag.system.model.dto.ProjectMemberDTO;
import top.teek.ag.system.model.dto.TeamMemberDTO;
import top.teek.ag.system.model.dto.TeamMemberWithProjectRoleDTO;
import top.teek.ag.system.model.po.ProjectMember;
import top.teek.ag.system.model.po.TeamMember;
import top.teek.ag.system.model.vo.TeamMemberVO;
import top.teek.ag.system.permission.PermissionHelper;
import top.teek.ag.system.service.ProjectMemberService;
import top.teek.ag.system.service.TeamMemberService;
import top.teek.core.exception.ServerException;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.utils.ListUtil;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2024-06-22 00:15:48
 * @note 针对表「t_team_member（团队成员表）」的数据库操作 Service 实现
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TeamMemberServiceImpl extends ServiceImpl<TeamMemberMapper, TeamMember> implements TeamMemberService {

    private final ProjectMemberService projectMemberService;

    @Override
    public List<TeamMemberVO> listAll(TeamMemberDTO teamMemberDTO) {
        LambdaQueryWrapper<TeamMember> wrapper = buildQueryWrapper(teamMemberDTO);
        List<TeamMember> teamMemberList = baseMapper.selectList(wrapper);

        return MapstructUtil.convert(teamMemberList, TeamMemberVO.class);
    }

    @Override
    public TablePage<TeamMemberVO> listPage(TeamMemberDTO teamMemberDTO, PageQuery pageQuery) {
        LambdaQueryWrapper<TeamMember> wrapper = buildQueryWrapper(teamMemberDTO);

        Page<TeamMember> teamMemberPage = baseMapper.selectPage(pageQuery.buildPage(), wrapper);

        return TablePage.build(teamMemberPage, TeamMemberVO.class);
    }

    private LambdaQueryWrapper<TeamMember> buildQueryWrapper(TeamMemberDTO teamMemberDTO) {
        return Wrappers.<TeamMember>lambdaQuery()
                .eq(StringUtil.hasText(teamMemberDTO.getTeamId()), TeamMember::getTeamId, teamMemberDTO.getTeamId())
                .eq(StringUtil.hasText(teamMemberDTO.getUsername()), TeamMember::getUsername, teamMemberDTO.getUsername())
                .eq(StringUtil.hasText(teamMemberDTO.getNickname()), TeamMember::getNickname, teamMemberDTO.getNickname())
                .eq(Objects.nonNull(teamMemberDTO.getTeamRole()), TeamMember::getTeamRole, teamMemberDTO.getTeamRole())
                .eq(Objects.nonNull(teamMemberDTO.getStatus()), TeamMember::getStatus, teamMemberDTO.getStatus())
                .orderByAsc(TeamMember::getTeamRole);
    }

    @Override
    public boolean addTeamMembers(List<TeamMemberDTO> teamMemberDTOList, String inviteUserId) {
        if (ListUtil.isEmpty(teamMemberDTOList)) {
            return false;
        }
        String teamId = teamMemberDTOList.get(0).getTeamId();
        if (StringUtil.hasText(inviteUserId)) {
            PermissionHelper.checkTeamOwnerAndAdmin(inviteUserId, teamId, "1h");
        }
        List<TeamMember> teamMemberList = MapstructUtil.convert(teamMemberDTOList, TeamMember.class);
        // 检查邀请的成员是否已经在团队里
        List<String> userIdList = teamMemberList.stream().map(TeamMember::getUserId).toList();
        boolean exists = baseMapper.exists(Wrappers.<TeamMember>lambdaQuery()
                .eq(TeamMember::getTeamId, teamId)
                .in(TeamMember::getUserId, userIdList));
        if (exists) {
            throw new ServerException("邀请的成员已在团队里");
        }

        return Db.saveBatch(teamMemberList);
    }

    @Override
    public boolean editTeamMemberRole(String teamId, String userId, int ordinal) {
        return baseMapper.update(new TeamMember(), Wrappers.<TeamMember>lambdaUpdate()
                .eq(TeamMember::getTeamId, teamId)
                .eq(TeamMember::getUserId, userId)
                .set(TeamMember::getTeamRole, ordinal)) > 0;
    }

    @Override
    public boolean leaveTeam(String teamId) {
        String userId = AgHelper.getUserId();
        if (StringUtil.hasEmpty(userId)) {
            log.error("无法获取登录的用户信息");
            return false;
        }
        return baseMapper.delete(Wrappers.<TeamMember>lambdaQuery()
                .eq(TeamMember::getTeamId, teamId)
                .eq(TeamMember::getUserId, userId)) > 0;
    }

    @Override
    public boolean removeAllTeamMember(String teamId) {
        return baseMapper.delete(Wrappers.<TeamMember>lambdaQuery()
                .eq(TeamMember::getTeamId, teamId)) > 0;
    }

    @Override
    public boolean checkMemberExist(String teamId, String userId) {
        return baseMapper.exists(Wrappers.<TeamMember>lambdaQuery()
                .eq(TeamMember::getTeamId, teamId)
                .eq(TeamMember::getUserId, userId));
    }

    @Override
    public boolean checkMemberRole(String teamId, String userId, List<Integer> ordinal) {
        return baseMapper.exists(Wrappers.<TeamMember>lambdaQuery()
                .eq(TeamMember::getTeamId, teamId)
                .eq(TeamMember::getUserId, userId)
                .in(TeamMember::getTeamRole, ordinal));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editTeamMemberWithProjectRole(TeamMemberWithProjectRoleDTO teamMemberWithProjectRoleDTO) {
        List<ProjectMemberDTO> projectMemberDTOList = teamMemberWithProjectRoleDTO.getProjectMemberList();
        if (ListUtil.isNotEmpty(projectMemberDTOList)) {
            List<ProjectMember> projectMemberList = MapstructUtil.convert(projectMemberDTOList, ProjectMember.class);
            projectMemberService.updateBatchById(projectMemberList);
        }
        TeamMember teamMember = MapstructUtil.convert(teamMemberWithProjectRoleDTO.getTeamMember(), TeamMember.class);
        String teamId = teamMember.getTeamId();
        teamMember.setTeamId(null);
        return baseMapper.update(teamMember, Wrappers.<TeamMember>lambdaQuery()
                .eq(TeamMember::getTeamId, teamId)) > 0;
    }
}
