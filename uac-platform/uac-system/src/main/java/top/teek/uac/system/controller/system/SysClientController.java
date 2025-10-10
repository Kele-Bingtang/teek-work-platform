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
import top.teek.uac.system.model.dto.SysClientDTO;
import top.teek.uac.system.model.vo.SysClientVO;
import top.teek.uac.system.model.vo.extra.ClientTreeVO;
import top.teek.uac.system.service.SysAppService;
import top.teek.uac.system.service.SysClientService;
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
 * @date 2023/11/26 22:47
 * @note
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/system/client")
public class SysClientController {

    private final SysClientService sysClientService;
    private final SysAppService sysAppService;

    @GetMapping("/list")
    @Operation(summary = "客户端列表查询", description = "通过客户端条件查询客户端列表）")
    @PreAuthorize("hasAuthority('system:client:list')")
    public Response<List<SysClientVO>> list(SysClientDTO sysClientDTO) {
        List<SysClientVO> sysClientVOList = sysClientService.listAll(sysClientDTO);
        return HttpResult.ok(sysClientVOList);
    }

    @GetMapping("/listPage")
    @Operation(summary = "客户端列表查询", description = "通过客户端条件查询客户端列表（支持分页）")
    @PreAuthorize("hasAuthority('system:client:list')")
    public Response<TablePage<SysClientVO>> listPage(SysClientDTO sysClientDTO, PageQuery pageQuery) {
        TablePage<SysClientVO> tablePage = sysClientService.listPage(sysClientDTO, pageQuery);
        return HttpResult.ok(tablePage);
    }

    @GetMapping("/treeList")
    @Operation(summary = "客户端树形列表查询", description = "查询客户端树形列表")
    @PreAuthorize("hasAuthority('system:client:query')")
    public Response<List<ClientTreeVO>> listTreeList() {
        List<ClientTreeVO> sysClientVoList = sysClientService.listTreeList();
        return HttpResult.ok(sysClientVoList);
    }

    @PostMapping
    @Operation(summary = "客户端新增", description = "新增客户端")
    @OperateLog(title = "客户端管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('system:client:add')")
    @PreventRepeatSubmit
    public Response<Boolean> insertClient(@Validated(RestGroup.AddGroup.class) @RequestBody SysClientDTO sysClientDTO) {
        if (sysClientService.checkClientKeyUnique(sysClientDTO)) {
            return HttpResult.failMessage("新增客户端「" + sysClientDTO.getClientName() + "」失败，客户端 Key「" + sysClientDTO.getClientKey() + "」已存在");
        } else if (sysClientService.checkClientSecretUnique(sysClientDTO)) {
            return HttpResult.failMessage("新增客户端「" + sysClientDTO.getClientName() + "」失败，客户端密钥「" + sysClientDTO.getClientSecret() + "」已存在");
        }

        return HttpResult.ok(sysClientService.insertClient(sysClientDTO));
    }

    /**
     * 客户端修改
     */
    @PutMapping
    @Operation(summary = "客户端修改", description = "修改客户端")
    @OperateLog(title = "客户端管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('system:client:edit')")
    @PreventRepeatSubmit
    public Response<Boolean> updateClient(@Validated(RestGroup.EditGroup.class) @RequestBody SysClientDTO sysClientDTO) {
        if (sysClientService.checkClientKeyUnique(sysClientDTO)) {
            return HttpResult.failMessage("修改客户端「" + sysClientDTO.getClientName() + "」失败，客户端 Key「" + sysClientDTO.getClientKey() + "」已存在");
        }
        if (sysClientService.checkClientSecretUnique(sysClientDTO)) {
            return HttpResult.failMessage("修改客户端「" + sysClientDTO.getClientName() + "」失败，客户端密钥「" + sysClientDTO.getClientSecret() + "」已存在");
        }

        return HttpResult.ok(sysClientService.updateClient(sysClientDTO));
    }

    @PutMapping("/updateStatus")
    @Operation(summary = "客户端状态修改", description = "修改客户端状态")
    @OperateLog(title = "客户端管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('system:client:edit')")
    @PreventRepeatSubmit
    public Response<Boolean> updateStatus(@RequestBody SysClientDTO sysClientDTO) {
        return HttpResult.ok(sysClientService.updateStatus(sysClientDTO.getId(), sysClientDTO.getStatus()));
    }

    @DeleteMapping("/{ids}")
    @Operation(summary = "客户端删除", description = "通过主键批量删除客户端")
    @OperateLog(title = "客户端管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('system:client:remove')")
    @PreventRepeatSubmit
    public Response<Boolean> removeBatch(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids, @RequestBody List<String> clientIds) {
        if (sysAppService.checkExitApp(clientIds)) {
            return HttpResult.failMessage("存在 APP 绑定，不允许删除");
        }

        return HttpResult.ok(sysClientService.removeBatch(List.of(ids)));
    }

    @PostMapping("/export")
    @Operation(summary = "客户端数据导出", description = "导出客户端数据")
    @OperateLog(title = "客户端管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("hasAuthority('system:client:export')")
    public void export(@RequestBody SysClientDTO sysClientDTO, HttpServletResponse response) {
        List<SysClientVO> sysClientVOList = sysClientService.listAll(sysClientDTO);
        ExcelHelper.exportExcel(sysClientVOList, "客户端数据", SysClientVO.class, response);
    }
}
