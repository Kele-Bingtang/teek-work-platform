package top.teek.ag.system.controller;

import top.teek.ag.core.helper.AgHelper;
import top.teek.ag.system.model.dto.ProjectMemberDTO;
import top.teek.ag.system.model.vo.ProjectMemberVO;
import top.teek.ag.system.permission.PermissionHelper;
import top.teek.ag.system.service.ProjectMemberService;
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
 * @date 2024/6/23 23:38:53
 * @note
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/projectMember")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @GetMapping("/listProjectRole/{teamId}/{userId}")
    @Operation(summary = "项目成员角色查询", description = "查询项目成员角色")
    public Response<List<ProjectMemberVO>> listProjectRole(@PathVariable String teamId, @PathVariable String userId) {
        List<ProjectMemberVO> projectMemberVOList = projectMemberService.listProjectRole(teamId, userId);
        return HttpResult.ok(projectMemberVOList);
    }

    @PostMapping("/getMyProjectRole/{projectId}")
    @Operation(summary = "我的项目角色查询", description = "查询我的项目角色")
    public Response<String> getMyProjectRole(@PathVariable String projectId) {
        String projectRole = projectMemberService.getMyProjectRole(projectId);
        return HttpResult.ok(projectRole);
    }

    @PostMapping
    @Operation(summary = "项目成员添加", description = "添加项目成员")
    public Response<Boolean> addProjectMember(@Validated(RestGroup.AddGroup.class) @RequestBody ProjectMemberDTO projectMemberDTO) {
        if (projectMemberService.checkMemberUnique(projectMemberDTO)) {
            return HttpResult.failMessage("新增项目成员失败，项目成员已存在");
        }

        // 检查是否有项目成员添加权限（只有团队所有者 | 管理员可以添加项目成员）
        PermissionHelper.checkTeamOwnerAndAdmin(AgHelper.getUserId(), projectMemberDTO.getTeamId(), "1h");
        PermissionHelper.checkProjectAdmin(AgHelper.getUserId(), projectMemberDTO.getTeamId(), "1h");

        return HttpResult.okOrFail(projectMemberService.addProjectMember(projectMemberDTO));
    }

    @PutMapping
    @Operation(summary = "项目成员编辑", description = "编辑项目成员")
    public Response<Boolean> editProjectMember(@Validated(RestGroup.EditGroup.class) @RequestBody ProjectMemberDTO projectMemberDTO) {
        // 检查是否有项目成员编辑权限（只有团队所有者 | 管理员可以编辑项目成员）
        PermissionHelper.checkTeamOwnerAndAdmin(AgHelper.getUserId(), projectMemberDTO.getTeamId(), "1h");
        PermissionHelper.checkProjectAdmin(AgHelper.getUserId(), projectMemberDTO.getTeamId(), "1h");

        return HttpResult.okOrFail(projectMemberService.editProjectMember(projectMemberDTO));
    }
    
}
