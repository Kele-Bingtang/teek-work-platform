package top.teek.ag.system.service.impl;

import top.teek.ag.core.constant.ColumnConstant;
import top.teek.ag.core.enums.BelongType;
import top.teek.ag.core.enums.ProjectMemberRole;
import top.teek.ag.core.enums.TeamMemberRole;
import top.teek.ag.core.helper.AgHelper;
import top.teek.ag.system.mapper.ProjectMapper;
import top.teek.ag.system.model.dto.CategoryDTO;
import top.teek.ag.system.model.dto.ProjectDTO;
import top.teek.ag.system.model.dto.ProjectMemberDTO;
import top.teek.ag.system.model.dto.TeamMemberDTO;
import top.teek.ag.system.model.po.Category;
import top.teek.ag.system.model.po.Project;
import top.teek.ag.system.model.po.ServiceCol;
import top.teek.ag.system.model.po.ServiceInfo;
import top.teek.ag.system.model.vo.ProjectVO;
import top.teek.ag.system.model.vo.TeamMemberVO;
import top.teek.ag.system.permission.PermissionHelper;
import top.teek.ag.system.service.*;
import top.teek.core.http.Assert;
import top.teek.utils.IdsUtil;
import top.teek.utils.ListUtil;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.teek.ag.system.service.*;

import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2024-06-22 00:15:48
 * @note 针对表「t_project（项目表）」的数据库操作 Service 实现
 */
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    private final TeamMemberService teamMemberService;
    private final ProjectMemberService projectMemberService;
    private final CategoryService categoryService;
    private final ServiceInfoService serviceInfoService;
    private final ServiceColService serviceColService;


    @Override
    public ProjectVO getByProjectId(String projectId) {
        Project project = baseMapper.selectOne(Wrappers.<Project>lambdaQuery()
                .eq(Project::getProjectId, projectId));
        Assert.isTrue(Objects.nonNull(project), "项目不存在");

        return MapstructUtil.convert(project, ProjectVO.class);
    }

    @Override
    public List<ProjectVO> listProject(ProjectDTO projectDTO) {
        QueryWrapper<Project> wrapper = buildQueryWrapper(projectDTO);
        List<Project> projectList = baseMapper.listProject(wrapper);

        return MapstructUtil.convert(projectList, ProjectVO.class);
    }

    private QueryWrapper<Project> buildQueryWrapper(ProjectDTO projectDTO) {
        final String projectTablePrefix = "tp.";
        final String projectMemberTablePrefix = "tpm.";
        return Wrappers.<Project>query()
                // project 表条件
                .eq(projectTablePrefix + "team_id", projectDTO.getTeamId())
                .eq(StringUtil.hasText(projectDTO.getProjectName()), projectTablePrefix + "project_name", projectDTO.getProjectName())
                .eq(Objects.nonNull(projectDTO.getStatus()), projectTablePrefix + "status", projectDTO.getStatus())
                .eq(projectTablePrefix + "is_deleted", 0)
                .orderByDesc(projectTablePrefix + "create_time")
                // project_member 表条件
                .eq(projectMemberTablePrefix + "user_id", AgHelper.getUserId())
                .eq(Objects.nonNull(projectDTO.getBelongType()) && projectDTO.getBelongType() != 0, projectMemberTablePrefix + "belong_type", projectDTO.getBelongType())
                .in(ListUtil.isNotEmpty(projectDTO.getDataSourceId()), projectTablePrefix + "data_source_id", projectDTO.getDataSourceId());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addProject(ProjectDTO projectDTO) {
        Project project = MapstructUtil.convert(projectDTO, Project.class);
        // 不带 - 的 UUID 作为项目密钥
        project.setSecretKey(IdsUtil.simpleUUID());
        int result = baseMapper.insert(project);

        // 生成默认目录
        CategoryDTO categoryDTO = new CategoryDTO()
                .setCategoryCode(ColumnConstant.DEFAULT_CATEGORY_CODE)
                .setCategoryName(ColumnConstant.DEFAULT_CATEGORY_NAME)
                .setProjectId(project.getProjectId())
                .setIsMain(1)
                .setTeamId(project.getTeamId());

        categoryService.addCategory(categoryDTO);

        // 团队的所有成员都可以看到项目
        addTeamMemberToProject(project.getProjectId(), project.getTeamId());
        return result > 0;
    }

    @Override
    public boolean editProject(ProjectDTO projectDTO) {
        Project project = MapstructUtil.convert(projectDTO, Project.class);
        return baseMapper.updateById(project) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeProject(String projectId) {
        Project project = baseMapper.selectOne(Wrappers.<Project>lambdaQuery()
                .select(Project::getProjectId)
                .eq(Project::getProjectId, projectId));

        Assert.nonNull(project, "删除的项目不存在");

        PermissionHelper.checkProjectAdmin(AgHelper.getUserId(), project.getProjectId(), "1h");

        // 删除项目目录
        categoryService.removeAllCategory(projectId);
        // 删除项目成员
        projectMemberService.removeAllMember(projectId);
        // 删除项目
        return baseMapper.delete(Wrappers.<Project>lambdaQuery()
                .eq(Project::getProjectId, projectId)) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeAllProject(String teamId) {
        // 删除团队下所有项目目录
        categoryService.removeAllCategoryByTeamId(teamId);
        // 删除团队下所有项目成员
        projectMemberService.removeAllMemberByTeamId(teamId);

        return baseMapper.delete(Wrappers.<Project>lambdaQuery()
                .eq(Project::getTeamId, teamId)) > 0;
    }

    @Override
    public boolean checkProjectNameUnique(ProjectDTO projectDTO) {
        return baseMapper.exists(Wrappers.<Project>lambdaQuery()
                .eq(Project::getProjectName, projectDTO.getProjectName())
                .eq(Project::getTeamId, projectDTO.getTeamId())
                .ne(Objects.nonNull(projectDTO.getId()), Project::getId, projectDTO.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean transferProject(String projectId, String teamId) {
        categoryService.update(Wrappers.<Category>lambdaUpdate()
                .set(Category::getTeamId, teamId)
                .eq(Category::getProjectId, projectId));

        serviceInfoService.update(Wrappers.<ServiceInfo>lambdaUpdate()
                .set(ServiceInfo::getTeamId, teamId)
                .eq(ServiceInfo::getProjectId, projectId));

        serviceColService.update(Wrappers.<ServiceCol>lambdaUpdate()
                .set(ServiceCol::getTeamId, teamId)
                .eq(ServiceCol::getProjectId, projectId));

        // reportService.update(Wrappers.<Report>lambdaUpdate()
        //         .set(Report::getTeamId, teamId)
        //         .eq(Report::getProjectId, projectId));

        // 添加项目成员，先删除旧项目成员
        projectMemberService.removeAllMember(projectId);
        addTeamMemberToProject(projectId, teamId);

        return baseMapper.update(Wrappers.<Project>lambdaUpdate()
                .set(Project::getTeamId, teamId)
                .eq(Project::getProjectId, projectId)) > 0;
    }

    private void addTeamMemberToProject(String projectId, String teamId) {
        // 团队的所有成员都可以看到项目
        List<TeamMemberVO> teamMemberVOList = teamMemberService.listAll(new TeamMemberDTO().setTeamId(teamId));
        List<ProjectMemberDTO> projectMemberDTOList = ListUtil.newArrayList(teamMemberVOList, teamMember ->
                        new ProjectMemberDTO()
                                .setProjectId(projectId)
                                .setUserId(teamMember.getUserId())
                                // 团队所有者 & 管理者默认是项目的管理者，团队普通成员默认是项目的普通成员
                                .setProjectRole(teamMember.getTeamRole() != TeamMemberRole.MEMBER.ordinal() ? ProjectMemberRole.ADMIN.ordinal() : ProjectMemberRole.MEMBER.ordinal())
                                .setBelongType(BelongType.CREATE.ordinal())
                                .setTeamId(teamMember.getTeamId())
                , ProjectMemberDTO.class);

        projectMemberService.addBatchProjectMember(projectMemberDTOList);
    }
}




