package top.teek.ag.system.service.impl;

import top.teek.ag.core.enums.ProjectMemberRole;
import top.teek.ag.core.helper.AgHelper;
import top.teek.ag.system.mapper.ProjectMemberMapper;
import top.teek.ag.system.model.dto.ProjectMemberDTO;
import top.teek.ag.system.model.po.ProjectMember;
import top.teek.ag.system.model.vo.ProjectMemberVO;
import top.teek.ag.system.service.ProjectMemberService;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2024-06-22 00:15:48
 * @note 针对表「t_project_member（项目成员表）」的数据库操作 Service 实现
 */
@Service
public class ProjectMemberServiceImpl extends ServiceImpl<ProjectMemberMapper, ProjectMember> implements ProjectMemberService {

    @Override
    public List<ProjectMemberVO> listProjectRole(String teamId, String userId) {
        QueryWrapper<ProjectMember> wrapper = Wrappers.<ProjectMember>query()
                .eq("tpm.team_id", teamId)
                .eq("tpm.user_id", userId)
                .eq("tpm.is_deleted", 0)
                .eq("tp.is_deleted", 0);

        return baseMapper.listProjectRole(wrapper);
    }

    @Override
    public String getMyProjectRole(String projectId) {
        ProjectMember projectMember = baseMapper.selectOne(Wrappers.<ProjectMember>lambdaQuery()
                .select(ProjectMember::getProjectRole)
                .eq(ProjectMember::getProjectId, projectId)
                .eq(ProjectMember::getUserId, AgHelper.getUserId()));

        if (Objects.isNull(projectMember)) {
            return "";
        }

        ProjectMemberRole projectMemberRole = ProjectMemberRole.getByOrdinal(projectMember.getProjectRole());
        if (Objects.isNull(projectMemberRole)) {
            return "";
        }
        return projectMemberRole.getLabel();
    }

    private LambdaQueryWrapper<ProjectMember> buildQueryWrapper(ProjectMemberDTO projectMemberDTO) {
        return Wrappers.<ProjectMember>lambdaQuery()
                .eq(StringUtil.hasText(projectMemberDTO.getTeamId()), ProjectMember::getTeamId, projectMemberDTO.getTeamId())
                .eq(StringUtil.hasText(projectMemberDTO.getUserId()), ProjectMember::getUserId, projectMemberDTO.getUserId())
                .eq(Objects.nonNull(projectMemberDTO.getProjectId()), ProjectMember::getProjectId, projectMemberDTO.getProjectId())
                .eq(Objects.nonNull(projectMemberDTO.getProjectRole()), ProjectMember::getProjectRole, projectMemberDTO.getProjectRole())
                .eq(Objects.nonNull(projectMemberDTO.getBelongType()), ProjectMember::getBelongType, projectMemberDTO.getBelongType())
                .orderByDesc(ProjectMember::getCreateTime);
    }

    @Override
    public boolean addProjectMember(ProjectMemberDTO projectMemberDTO) {
        ProjectMember projectMember = MapstructUtil.convert(projectMemberDTO, ProjectMember.class);
        return baseMapper.insert(projectMember) > 0;
    }

    @Override
    public boolean addBatchProjectMember(List<ProjectMemberDTO> projectMemberDTOList) {
        List<ProjectMember> projectMemberList = MapstructUtil.convert(projectMemberDTOList, ProjectMember.class);
        return saveBatch(projectMemberList);
    }

    @Override
    public boolean editProjectMember(ProjectMemberDTO projectMemberDTO) {
        ProjectMember projectMember = MapstructUtil.convert(projectMemberDTO, ProjectMember.class);
        return baseMapper.updateById(projectMember) > 0;
    }

    @Override
    public boolean checkMemberRole(String userId, String projectId, List<Integer> ordinal) {
        return baseMapper.exists(Wrappers.<ProjectMember>lambdaQuery()
                .eq(ProjectMember::getUserId, userId)
                .eq(ProjectMember::getProjectId, projectId)
                .in(ProjectMember::getProjectRole, ordinal));
    }

    @Override
    public boolean checkMemberExist(String userId, String projectId) {
        return baseMapper.exists(Wrappers.<ProjectMember>lambdaQuery()
                .eq(ProjectMember::getUserId, userId)
                .eq(ProjectMember::getProjectId, projectId));
    }

    @Override
    public boolean removeAllMember(String projectId) {
        return baseMapper.delete(Wrappers.<ProjectMember>lambdaQuery()
                .eq(ProjectMember::getProjectId, projectId)) > 0;
    }

    @Override
    public boolean removeAllMemberByTeamId(String teamId) {
        return baseMapper.delete(Wrappers.<ProjectMember>lambdaQuery()
                .eq(ProjectMember::getTeamId, teamId)) > 0;
    }

    @Override
    public boolean checkMemberUnique(ProjectMemberDTO projectMemberDTO) {
        return baseMapper.exists(Wrappers.<ProjectMember>lambdaQuery()
                .eq(ProjectMember::getTeamId, projectMemberDTO.getTeamId())
                .eq(ProjectMember::getUserId, projectMemberDTO.getUserId())
                .eq(ProjectMember::getProjectId, projectMemberDTO.getProjectId()));
    }
}




