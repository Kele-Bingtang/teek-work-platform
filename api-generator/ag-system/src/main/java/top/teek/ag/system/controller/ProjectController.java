package top.teek.ag.system.controller;

import top.teek.ag.system.model.dto.ProjectDTO;
import top.teek.ag.system.model.vo.ProjectVO;
import top.teek.ag.system.permission.annotation.ProjectAuthorize;
import top.teek.ag.system.permission.annotation.TeamAuthorize;
import top.teek.ag.system.service.ProjectService;
import top.teek.ag.system.service.TeamMemberService;
import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.core.validate.RestGroup;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/6/24 22:33:45
 * @note 项目相关接口
 */
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
@Validated
public class ProjectController {

    private final ProjectService projectService;
    private final TeamMemberService teamMemberService;

    @GetMapping("/getByProjectId/{projectId}")
    @Operation(summary = "通过项目 ID 查询一笔项目数据", description = "通过项目 ID 查询一笔项目数据")
    @ProjectAuthorize(value = "#projectId", checkRead = true)
    public Response<ProjectVO> getByProjectId(@PathVariable String projectId) {
        ProjectVO project = projectService.getByProjectId(projectId);
        return HttpResult.ok(project);
    }

    @GetMapping("/list")
    @Operation(summary = "个人项目列表查询", description = "查询个人项目列表")
    public Response<List<ProjectVO>> listProject(@Validated(RestGroup.QueryGroup.class) ProjectDTO projectDTO) {
        List<ProjectVO> projectVOList = projectService.listProject(projectDTO);
        return HttpResult.ok(projectVOList);
    }

    @PostMapping
    @Operation(summary = "项目新增", description = "新增项目")
    @TeamAuthorize(value = "#projectDTO.getTeamId()", checkOwnerAndAdmin = true)
    public Response<Boolean> addProject(@Validated(RestGroup.AddGroup.class) @RequestBody ProjectDTO projectDTO) {
        if (projectService.checkProjectNameUnique(projectDTO)) {
            return HttpResult.failMessage("新增项目「" + projectDTO.getProjectName() + "」失败，项目名称已存在");
        }

        boolean result = projectService.addProject(projectDTO);
        return HttpResult.okOrFail(result);
    }

    @PutMapping
    @Operation(summary = "项目编辑", description = "编辑项目")
    @ProjectAuthorize(value = "#projectDTO.getProjectId()", checkAdmin = true)
    public Response<Boolean> editProject(@Validated(RestGroup.EditGroup.class) @RequestBody ProjectDTO projectDTO) {
        if (projectService.checkProjectNameUnique(projectDTO)) {
            return HttpResult.failMessage("编辑项目「" + projectDTO.getProjectName() + "」失败，项目名称已存在");
        }

        boolean result = projectService.editProject(projectDTO);
        return HttpResult.okOrFail(result);
    }

    @DeleteMapping("/{projectId}")
    @Operation(summary = "项目删除", description = "删除项目")
    @ProjectAuthorize(value = "#projectId", checkAdmin = true)
    public Response<Boolean> removeProject(@PathVariable String projectId) {
        boolean result = projectService.removeProject(projectId);
        return HttpResult.okOrFail(result);
    }
    
    @PostMapping("/transferProject/{projectId}/{teamId}")
    @Operation(summary = "项目转移", description = "转移项目")
    @ProjectAuthorize(value = "#projectId", checkAdmin = true)
    public Response<Boolean> transferProject(@PathVariable String projectId, @PathVariable String teamId) {
        boolean result = projectService.transferProject(projectId, teamId);
        return HttpResult.okOrFail(result);
    }

}
