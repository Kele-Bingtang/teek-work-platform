package top.teek.ag.system.controller;

import top.teek.ag.system.model.dto.TeamMemberDTO;
import top.teek.ag.system.model.dto.TeamMemberWithProjectRoleDTO;
import top.teek.ag.system.model.vo.TeamMemberVO;
import top.teek.ag.system.permission.annotation.TeamAuthorize;
import top.teek.ag.system.service.TeamMemberService;
import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.core.validate.RestGroup;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/6/23 00:10:28
 * @note
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/teamMember")
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    @GetMapping("/listAll")
    @Operation(summary = "团队成员查询", description = "查询团队成员")
    @TeamAuthorize(value = "#teamMemberDTO.getTeamId()", checkOwnerAndAdmin = true)
    public Response<List<TeamMemberVO>> listAll(@Validated(RestGroup.QueryGroup.class) TeamMemberDTO teamMemberDTO) {
        List<TeamMemberVO> teamMemberVOList = teamMemberService.listAll(teamMemberDTO);
        return HttpResult.ok(teamMemberVOList);
    }

    @GetMapping("/listPage")
    @Operation(summary = "团队成员分页查询", description = "分页查询团队路由")
    @TeamAuthorize(value = "#teamMemberDTO.getTeamId()", checkOwnerAndAdmin = true)
    public Response<TablePage<TeamMemberVO>> listPage(@Validated(RestGroup.QueryGroup.class) TeamMemberDTO teamMemberDTO, PageQuery pageQuery) {
        TablePage<TeamMemberVO> teamMemberVOList = teamMemberService.listPage(teamMemberDTO, pageQuery);
        return HttpResult.ok(teamMemberVOList);
    }

    @PostMapping("/inviteMembers")
    @Operation(summary = "团队成员邀请", description = "邀请团队成员")
    public Response<Boolean> inviteMembers(@RequestBody List<TeamMemberDTO> teamMemberDTOList, String inviteUserId) {
        return HttpResult.ok(teamMemberService.addTeamMembers(teamMemberDTOList, inviteUserId));
    }

    @DeleteMapping("/{teamId}")
    @Operation(summary = "团队退出", description = "退出团队")
    public Response<Boolean> leaveTeam(@PathVariable String teamId) {
        return HttpResult.ok(teamMemberService.leaveTeam(teamId));
    }

    @PutMapping
    @Operation(summary = "团队成员角色修改", description = "修改团队成员角色")
    @TeamAuthorize(value = "#teamMemberWithProjectRoleDTO.getTeamMember.getTeamId()", checkOwnerAndAdmin = true)
    public Response<Boolean> editTeamMemberWithProjectRole(@RequestBody TeamMemberWithProjectRoleDTO teamMemberWithProjectRoleDTO) {
        return HttpResult.ok(teamMemberService.editTeamMemberWithProjectRole(teamMemberWithProjectRoleDTO));
    }
}
