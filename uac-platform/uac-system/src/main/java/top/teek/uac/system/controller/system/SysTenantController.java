package top.teek.uac.system.controller.system;

import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.core.validate.RestGroup;
import top.teek.excel.helper.ExcelHelper;
import top.teek.idempotent.annotation.PreventRepeatSubmit;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.core.constant.TenantConstant;
import top.teek.uac.core.log.annotation.OperateLog;
import top.teek.uac.core.log.enums.BusinessType;
import top.teek.uac.system.model.dto.SysTenantDTO;
import top.teek.uac.system.model.vo.SysTenantVO;
import top.teek.uac.system.service.SysTenantService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Teeker
 * @date 2023/12/4 19:23
 * @note
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/tenant")
public class SysTenantController {

    private final SysTenantService sysTenantService;

    @GetMapping("/list")
    @Operation(summary = "租户列表查询", description = "通过条件查询租户列表")
    @PreAuthorize("hasAuthority('system:tenant:list')")
    public Response<List<SysTenantVO>> list(SysTenantDTO sysTenantDTO) {
        List<SysTenantVO> sysTenantVOList = sysTenantService.listAll(sysTenantDTO);
        return HttpResult.ok(sysTenantVOList);
    }

    @GetMapping("/listPage")
    @Operation(summary = "租户列表查询", description = "通过条件查询租户列表")
    @PreAuthorize("hasAuthority('system:tenant:list')")
    public Response<TablePage<SysTenantVO>> listPage(SysTenantDTO sysTenantDTO, PageQuery pageQuery) {
        TablePage<SysTenantVO> tablePage = sysTenantService.listPage(sysTenantDTO, pageQuery);
        return HttpResult.ok(tablePage);
    }


    @PostMapping
    @Operation(summary = "租户列表新增", description = "新增租户")
    @OperateLog(title = "租户管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('system:tenant:add')")
    @PreventRepeatSubmit
    public Response<Boolean> addTenant(@Validated(RestGroup.AddGroup.class) @RequestBody SysTenantDTO sysTenantDTO) {
        if (sysTenantService.checkCompanyNameUnique(sysTenantDTO)) {
            return HttpResult.failMessage("新增租户'" + sysTenantDTO.getTenantName() + "'失败，租户已存在");
        }
        return HttpResult.ok(sysTenantService.addTenant(sysTenantDTO));
    }

    @PutMapping
    @Operation(summary = "租户列表修改", description = "修改租户")
    @OperateLog(title = "租户管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('system:tenant:edit')")
    @PreventRepeatSubmit
    public Response<Boolean> editTenant(@Validated(RestGroup.EditGroup.class) @RequestBody SysTenantDTO sysTenantDTO) {
        if (sysTenantService.checkCompanyNameUnique(sysTenantDTO)) {
            return HttpResult.failMessage("修改租户'" + sysTenantDTO.getTenantName() + "'失败，租户已存在");
        }
        return HttpResult.ok(sysTenantService.editTenant(sysTenantDTO));
    }

    @DeleteMapping("/{ids}")
    @Operation(summary = "租户列表删除", description = "通过主键批量删除租户")
    @OperateLog(title = "租户管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('system:tenant:remove')")
    @PreventRepeatSubmit
    public Response<Boolean> removeBatch(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids, @RequestBody List<String> tenantIds) {
        if (tenantIds.contains(TenantConstant.DEFAULT_TENANT_ID)) {
            return HttpResult.failMessage("初始租户不允许删除");
        }
        return HttpResult.ok(sysTenantService.removeBatch(List.of(ids)));
    }

    @PostMapping("/export")
    @Operation(summary = "租户数据导出", description = "导出租户数据")
    @OperateLog(title = "租户数据管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("hasAuthority('system:tenant:export')")
    public void export(@RequestBody SysTenantDTO sysTenantDTO, HttpServletResponse response) {
        List<SysTenantVO> sysTenantVOList = sysTenantService.listAll(sysTenantDTO);
        ExcelHelper.exportExcel(sysTenantVOList, "租户数据", SysTenantVO.class, response);
    }
}
