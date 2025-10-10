package top.teek.uac.system.controller.system;

import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.core.validate.RestGroup;
import top.teek.excel.helper.ExcelHelper;
import top.teek.idempotent.annotation.PreventRepeatSubmit;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.core.log.annotation.OperateLog;
import top.teek.uac.core.log.enums.BusinessType;
import top.teek.uac.system.model.dto.SysAppDTO;
import top.teek.uac.system.model.vo.SysAppVO;
import top.teek.uac.system.model.vo.extra.AppTreeVO;
import top.teek.uac.system.service.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.teek.uac.system.service.*;

import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2023/11/26 22:44
 * @note
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/app")
public class SysAppController {

    private final SysAppService sysAppService;
    private final SysClientService sysClientService;
    private final SysRoleService sysRoleService;
    private final SysMenuService sysMenuService;
    private final SysDictTypeService sysDictTypeService;

    @GetMapping("/list")
    @Operation(summary = "应用列表查询", description = "通过应用条件查询应用列表")
    @PreAuthorize("hasAuthority('system:app:list')")
    public Response<List<SysAppVO>> list(SysAppDTO sysAppDTO) {
        List<SysAppVO> sysAppVOList = sysAppService.listAll(sysAppDTO);
        return HttpResult.ok(sysAppVOList);
    }

    @GetMapping("/listPage")
    @Operation(summary = "应用列表查询", description = "通过应用条件查询应用列表（支持分页）")
    @PreAuthorize("hasAuthority('system:app:list')")
    public Response<TablePage<SysAppVO>> listPage(SysAppDTO sysAppDTO, PageQuery pageQuery) {
        TablePage<SysAppVO> tablePage = sysAppService.listPage(sysAppDTO, pageQuery);
        return HttpResult.ok(tablePage);
    }

    @GetMapping("/list/{clientId}")
    @Operation(summary = "应用列表查询", description = "通过客户端 ID 查询 App 清单列表")
    @PreAuthorize("hasAuthority('system:app:list')")
    public Response<TablePage<SysAppVO>> listByClientId(@NotNull(message = "客户端 ID 不能为空") @PathVariable String clientId, PageQuery pageQuery) {
        if (Objects.isNull(sysClientService.checkClientIdThenGet(clientId))) {
            return HttpResult.errorMessage("客户端 ID 不存在");
        }
        SysAppDTO sysAppDTO = SysAppDTO.builder().clientId(clientId).build();
        TablePage<SysAppVO> tablePage = sysAppService.listPage(sysAppDTO, pageQuery);
        return HttpResult.ok(tablePage);
    }

    @GetMapping("/treeList")
    @Operation(summary = "应用树形列表查询", description = "查询应用树形列表")
    @PreAuthorize("hasAuthority('system:app:query')")
    public Response<List<AppTreeVO>> listTreeList() {
        List<AppTreeVO> appTreeVOList = sysAppService.listTreeList();
        return HttpResult.ok(appTreeVOList);
    }

    @PostMapping
    @Operation(summary = "应用新增", description = "新增应用")
    @OperateLog(title = "应用管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('system:app:add')")
    @PreventRepeatSubmit
    public Response<Boolean> addApp(@Validated(RestGroup.AddGroup.class) @RequestBody SysAppDTO sysAppDTO) {
        if (sysAppService.checkAppCodeUnique(sysAppDTO)) {
            return HttpResult.failMessage("新增应用「" + sysAppDTO.getAppName() + "」失败，应用编码「" + sysAppDTO.getAppCode() + "」已存在");
        }
        
        return HttpResult.ok(sysAppService.addApp(sysAppDTO));
    }

    @PutMapping
    @Operation(summary = "应用修改", description = "修改应用")
    @OperateLog(title = "应用管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('system:app:edit')")
    @PreventRepeatSubmit
    public Response<Boolean> editApp(@Validated(RestGroup.EditGroup.class) @RequestBody SysAppDTO sysAppDTO) {
        if (sysAppService.checkAppCodeUnique(sysAppDTO)) {
            return HttpResult.failMessage("修改应用「" + sysAppDTO.getAppName() + "」失败，应用编码「" + sysAppDTO.getAppCode() + "」已存在");
        }
        return HttpResult.ok(sysAppService.editApp(sysAppDTO));
    }

    @DeleteMapping("/{ids}")
    @Operation(summary = "应用删除", description = "通过主键批量删除应用")
    @OperateLog(title = "应用管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('system:app:remove')")
    @PreventRepeatSubmit
    public Response<Boolean> removeBatch(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids, @RequestBody List<String> appIds) {
        if (sysRoleService.checkAppExitRole(appIds)) {
            return HttpResult.failMessage("存在角色绑定，不允许删除");
        }

        if (sysMenuService.checkAppExitMenu(appIds)) {
            return HttpResult.failMessage("存在菜单绑定，不允许删除");
        }

        if (sysDictTypeService.checkAppExitDictType(appIds)) {
            return HttpResult.failMessage("存在字典类型绑定，不允许删除");
        }

        return HttpResult.ok(sysAppService.removeBatch(List.of(ids)));
    }

    @PostMapping("/export")
    @Operation(summary = "应用数据导出", description = "导出应用数据")
    @OperateLog(title = "应用管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("hasAuthority('system:app:export')")
    public void export(@RequestBody SysAppDTO sysAppDTO, HttpServletResponse response) {
        List<SysAppVO> sysAppVOList = sysAppService.listAll(sysAppDTO);
        ExcelHelper.exportExcel(sysAppVOList, "应用数据", SysAppVO.class, response);
    }
}
