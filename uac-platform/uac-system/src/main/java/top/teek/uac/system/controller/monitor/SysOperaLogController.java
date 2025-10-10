package top.teek.uac.system.controller.monitor;

import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.excel.helper.ExcelHelper;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.core.log.annotation.OperateLog;
import top.teek.uac.core.log.enums.BusinessType;
import top.teek.uac.system.model.dto.SysOperaLogDTO;
import top.teek.uac.system.model.vo.SysOperaLogVO;
import top.teek.uac.system.service.SysOperaLogService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/4/27 19:06:22
 * @note 操作日志 Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/monitor/operaLog")
public class SysOperaLogController {

    private final SysOperaLogService sysOperaLogService;

    @GetMapping("/listPage")
    @Operation(summary = "操作日志列表查询", description = "通过条件查询登录操作日志（支持分页）")
    @PreAuthorize("hasAuthority('system:operaLog:list')")
    public Response<TablePage<SysOperaLogVO>> listPage(SysOperaLogDTO sysOperaLogDTO, PageQuery pageQuery) {
        TablePage<SysOperaLogVO> tablePage = sysOperaLogService.listPage(sysOperaLogDTO, pageQuery);
        return HttpResult.ok(tablePage);
    }

    @DeleteMapping("/{ids}")
    @Operation(summary = "操作日志删除", description = "通过主键批量删除操作日志")
    @OperateLog(title = "操作日志管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('system:operaLog:remove')")
    public Response<Boolean> removeBatch(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return HttpResult.ok(sysOperaLogService.removeBatch(List.of(ids)));
    }

    @DeleteMapping("/clean")
    @Operation(summary = "操作日志清除", description = "清除所有操作日志")
    @OperateLog(title = "操作日志管理", businessType = BusinessType.CLEAN)
    @PreAuthorize("hasAuthority('system:operaLog:remove')")
    public Response<Boolean> cleanAllOperaLog() {
        return HttpResult.ok(sysOperaLogService.cleanAllOperaLog());
    }

    @PostMapping("/export")
    @Operation(summary = "操作日志数据导出", description = "导出操作日志数据")
    @OperateLog(title = "操作日志管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("hasAuthority('system:operaLog:export')")
    public void export(@RequestBody SysOperaLogDTO sysOperaLogDTO, HttpServletResponse response) {
        List<SysOperaLogVO> sysOperaLogVOList = sysOperaLogService.listAll(sysOperaLogDTO);
        ExcelHelper.exportExcel(sysOperaLogVOList, "操作日志", SysOperaLogVO.class, response);
    }
}
