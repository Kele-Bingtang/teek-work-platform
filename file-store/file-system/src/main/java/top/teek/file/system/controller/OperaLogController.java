package top.teek.file.system.controller;

import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.excel.helper.ExcelHelper;
import top.teek.file.system.model.dto.FileOperaLogDTO;
import top.teek.file.system.model.vo.FileOperaLogVO;
import top.teek.file.system.service.FileOperaLogService;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/8/11 23:30:18
 * @note
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/operaLog")
public class OperaLogController {
    private final FileOperaLogService fileOperaLogService;

    @GetMapping("/listPage")
    @Operation(summary = "操作日志列表查询", description = "通过条件查询登录操作日志（支持分页）")
    @PreAuthorize("hasAuthority('system:operaLog:list')")
    public Response<TablePage<FileOperaLogVO>> listPage(FileOperaLogDTO fileOperaLogDTO, PageQuery pageQuery) {
        TablePage<FileOperaLogVO> tablePage = fileOperaLogService.listPage(fileOperaLogDTO, pageQuery);
        return HttpResult.ok(tablePage);
    }

    @DeleteMapping("/{ids}")
    @Operation(summary = "操作日志删除", description = "通过主键批量删除操作日志")
    @PreAuthorize("hasAuthority('system:operaLog:remove')")
    public Response<Boolean> removeBatch(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return HttpResult.ok(fileOperaLogService.removeBatch(List.of(ids)));
    }

    @DeleteMapping("/clean")
    @Operation(summary = "操作日志清除", description = "清除所有操作日志")
    @PreAuthorize("hasAuthority('system:operaLog:remove')")
    public Response<Boolean> cleanAllOperaLog() {
        return HttpResult.ok(fileOperaLogService.cleanAllOperaLog());
    }

    @PostMapping("/export")
    @Operation(summary = "操作日志数据导出", description = "导出操作日志数据")
    @PreAuthorize("hasAuthority('system:operaLog:export')")
    public void export(@RequestBody FileOperaLogDTO fileOperaLogDTO, HttpServletResponse response) {
        List<FileOperaLogVO> sysOperaLogVOList = fileOperaLogService.listAll(fileOperaLogDTO);
        ExcelHelper.exportExcel(sysOperaLogVOList, "操作日志", FileOperaLogVO.class, response);
    }
}
