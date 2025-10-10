package top.teek.ag.system.controller;

import top.teek.ag.system.model.dto.ReportDTO;
import top.teek.ag.system.model.vo.ReportDataVO;
import top.teek.ag.system.model.vo.ReportVO;
import top.teek.ag.system.permission.annotation.ProjectAuthorize;
import top.teek.ag.system.service.ReportService;
import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.core.validate.RestGroup;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Teeker
 * @date 2024/6/26 00:05:23
 * @note
 */
@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/getReportByServiceId/{serviceId}")
    @Operation(summary = "报表查询", description = "通过条件查询报表")
    public Response<ReportVO> getReportByServiceId(@PathVariable String serviceId) {
        ReportVO reportVO = reportService.getReportByServiceId(serviceId);
        return HttpResult.ok(reportVO);
    }

    @PutMapping
    @Operation(summary = "报表修改", description = "修改报表")
    @ProjectAuthorize(value = "#reportDTO.getProjectId()", checkReadAndWrite = true)
    public Response<Boolean> editReport(@Validated(RestGroup.EditGroup.class) @RequestBody ReportDTO reportDTO) {
        if (reportService.checkReportTitleUnique(reportDTO)) {
            return HttpResult.failMessage("编辑报表「" + reportDTO.getReportTitle() + "」失败，报表标题重复");
        }

        return HttpResult.okOrFail(reportService.editReport(reportDTO));
    }

    @GetMapping(value = "/listReportConfig/{serviceId}")
    @Operation(summary = "查询报表数据", description = "查询报表数据")
    public Response<ReportDataVO> listReportConfig(@PathVariable String serviceId) {
        ReportDataVO reportDataVO = reportService.listReportConfig(serviceId);
        return HttpResult.ok(reportDataVO);
    }
    
}
