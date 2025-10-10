package top.teek.ag.system.controller;

import top.teek.ag.system.model.vo.ProjectStatisticVO;
import top.teek.ag.system.service.ProjectStatisticService;
import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Teeker
 * @date 2024/8/4 15:37:28
 * @note
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/projectStatistic")
public class ProjectStatisticController {
    
    private final ProjectStatisticService projectStatisticService;
    
    @GetMapping("/getBaseProjectStatistic/{projectId}")
    @Operation(summary = "项目基础统计", description = "项目基础统计")
    public Response<ProjectStatisticVO> getBaseProjectStatistic(@PathVariable String projectId) {
        ProjectStatisticVO projectStatisticVO = projectStatisticService.getBaseProjectStatistic(projectId);
        return HttpResult.ok(projectStatisticVO);
    }
}
