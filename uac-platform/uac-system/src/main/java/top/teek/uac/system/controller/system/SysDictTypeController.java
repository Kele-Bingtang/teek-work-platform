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
import top.teek.uac.system.model.dto.SysDictTypeDTO;
import top.teek.uac.system.model.po.SysDictType;
import top.teek.uac.system.model.vo.SysDictTypeVO;
import top.teek.uac.system.service.SysDictDataService;
import top.teek.uac.system.service.SysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Teeker
 * @date 2023/12/4 16:29
 * @note
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/dict/type")
public class SysDictTypeController {

    private final SysDictTypeService sysDictTypeService;
    private final SysDictDataService sysDictDataService;

    @GetMapping("/list")
    @Operation(summary = "字典类型列表查询", description = "通过应用条件查询字典类型列表")
    @PreAuthorize("hasAuthority('system:dict:list')")
    public Response<List<SysDictTypeVO>> list(SysDictTypeDTO sysDictTypeDTO) {
        List<SysDictTypeVO> sysDictTypeVOList = sysDictTypeService.listAll(sysDictTypeDTO);
        return HttpResult.ok(sysDictTypeVOList);
    }

    @GetMapping("/listPage")
    @Operation(summary = "字典类型列表查询", description = "通过应用条件查询字典类型列表（支持分页）")
    @PreAuthorize("hasAuthority('system:dict:list')")
    public Response<TablePage<SysDictTypeVO>> listPage(SysDictTypeDTO sysDictTypeDTO, PageQuery pageQuery) {
        TablePage<SysDictTypeVO> tablePage = sysDictTypeService.listPage(sysDictTypeDTO, pageQuery);
        return HttpResult.ok(tablePage);
    }

    @PostMapping
    @Operation(summary = "字典类型列表新增", description = "新增字典类型列表")
    @OperateLog(title = "字典类型管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('system:dict:add')")
    @PreventRepeatSubmit
    public Response<Boolean> addDictType(@Validated(RestGroup.AddGroup.class) @RequestBody SysDictTypeDTO sysDictTypeDTO) {
        if (sysDictTypeService.checkDictCodeUnique(sysDictTypeDTO)) {
            return HttpResult.failMessage("新增字典类型编码「" + sysDictTypeDTO.getDictCode() + "」失败，字典字典类型编码已存在");
        }

        return HttpResult.ok(sysDictTypeService.addDictType(sysDictTypeDTO));
    }

    @PutMapping
    @Operation(summary = "字典类型列表修改", description = "修改字典类型列表")
    @OperateLog(title = "字典类型管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('system:dict:edit')")
    @PreventRepeatSubmit
    public Response<Boolean> editDictType(@Validated(RestGroup.EditGroup.class) @RequestBody SysDictTypeDTO sysDictTypeDTO) {
        if (sysDictTypeService.checkDictCodeUnique(sysDictTypeDTO)) {
            return HttpResult.failMessage("修改字典类型编码「" + sysDictTypeDTO.getDictCode() + "」失败，字典字典类型编码已存在");
        }

        return HttpResult.ok(sysDictTypeService.editDictType(sysDictTypeDTO));
    }

    /**
     * 客户端删除
     */
    @DeleteMapping("/{ids}")
    @Operation(summary = "字典类型列表删除", description = "通过主键批量删除字典类型列表")
    @OperateLog(title = "字典类型管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('system:dict:remove')")
    @PreventRepeatSubmit
    public Response<Boolean> removeBatch(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        List<Long> idList = List.of(ids);
        List<SysDictType> sysDictTypes = sysDictDataService.checkDictTypeExitDataAndGet(idList);

        if (!sysDictTypes.isEmpty()) {
            String dictNames = sysDictTypes.stream()
                    .map(SysDictType::getDictName)
                    .distinct()
                    .collect(Collectors.joining(","));

            return HttpResult.failMessage(dictNames + " 已分配字典数据，不允许删除");
        }
        return HttpResult.ok(sysDictTypeService.removeBatch(idList));
    }

    @PostMapping("/export")
    @Operation(summary = "字典类型导出", description = "导出字典类型")
    @OperateLog(title = "字典类型管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("hasAuthority('system:dict:export')")
    public void export(@RequestBody SysDictTypeDTO sysDictTypeDTO, HttpServletResponse response) {
        List<SysDictTypeVO> sysDictTypeVOList = sysDictTypeService.listAll(sysDictTypeDTO);
        ExcelHelper.exportExcel(sysDictTypeVOList, "字典类型数据", SysDictTypeVO.class, response);
    }
}
