package top.teek.ag.system.controller;

import top.teek.ag.system.model.dto.TeamDTO;
import top.teek.ag.system.model.vo.TeamRouteVO;
import top.teek.ag.system.model.vo.router.RouterVO;
import top.teek.ag.system.permission.annotation.TeamAuthorize;
import top.teek.ag.system.service.TeamMemberService;
import top.teek.ag.system.service.TeamService;
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
 * @date 2024/6/22 13:56:52
 * @note
 */
@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final TeamMemberService teamMemberService;

    @GetMapping("/listMyAllTeamRoute")
    @Operation(summary = "团队路由查询", description = "查询团队路由")
    public Response<List<RouterVO>> listMyAllTeamRoute() {
        List<RouterVO> routerVOList = teamService.listMyAllTeamRoute();
        return HttpResult.ok(routerVOList);
    }

    @GetMapping("/listMyAllTeam")
    @Operation(summary = "我的团队列表查询", description = "查询我的团队列表")
    public Response<List<TeamRouteVO>> listMyAll() {
        List<TeamRouteVO> teamRouteVOList = teamService.listMyAll();
        return HttpResult.ok(teamRouteVOList);
    }

    @PostMapping
    @Operation(summary = "团队新增", description = "新增团队")
    public Response<Boolean> addTeam(@Validated(RestGroup.AddGroup.class) @RequestBody TeamDTO teamDTO) {
        if (teamService.checkTeamNameUnique(teamDTO)) {
            return HttpResult.failMessage("新增团队「" + teamDTO.getTeamName() + "」失败，团队名称已存在");
        }

        return HttpResult.okOrFail(teamService.addTeam(teamDTO));
    }

    @PutMapping
    @Operation(summary = "团队编辑", description = "编辑团队")
    @TeamAuthorize(value = "#teamDTO.getTeamId()", checkOwner = true)
    public Response<Boolean> editTeam(@Validated(RestGroup.EditGroup.class) @RequestBody TeamDTO teamDTO) {
        if (teamService.checkTeamNameUnique(teamDTO)) {
            return HttpResult.failMessage("编辑团队「" + teamDTO.getTeamName() + "」失败，团队名称已存在");
        }

        return HttpResult.okOrFail(teamService.editTeam(teamDTO));
    }

    @DeleteMapping("/{teamId}")
    @Operation(summary = "团队解散", description = "解散团队")
    @TeamAuthorize(value = "#teamId", checkOwner = true)
    public Response<Boolean> removeTeam(@PathVariable String teamId) {
        return HttpResult.okOrFail(teamService.removeTeam(teamId));
    }

    @PostMapping("/transferOwner/{teamId}/{userId}/{username}")
    @Operation(summary = "团队负责人移交", description = "移交团队负责人")
    @TeamAuthorize(value = "#teamId", checkOwner = true)
    public Response<Boolean> transferOwner(@PathVariable String teamId, @PathVariable String userId, @PathVariable String username) {
        boolean result = teamService.transferOwner(teamId, userId, username);
        return HttpResult.okOrFail(result);
    }

}
