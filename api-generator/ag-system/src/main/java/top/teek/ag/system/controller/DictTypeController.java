package top.teek.ag.system.controller;

import top.teek.ag.system.model.dto.DictTypeDTO;
import top.teek.ag.system.model.po.DictType;
import top.teek.ag.system.model.vo.DictTypeVO;
import top.teek.ag.system.service.DictDataService;
import top.teek.ag.system.service.DictTypeService;
import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.core.validate.RestGroup;
import top.teek.excel.helper.ExcelHelper;
import top.teek.idempotent.annotation.PreventRepeatSubmit;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/dict/type")
public class DictTypeController {

    private final DictTypeService dictTypeService;
    private final DictDataService dictDataService;

    @GetMapping("/list")
    @Operation(summary = "字典类型列表查询", description = "通过应用条件查询字典类型列表")
    public Response<List<DictTypeVO>> list(DictTypeDTO dictTypeDTO) {
        List<DictTypeVO> dictTypeVOList = dictTypeService.listAll(dictTypeDTO);
        return HttpResult.ok(dictTypeVOList);
    }

    @GetMapping("/listPage")
    @Operation(summary = "字典类型列表查询", description = "通过应用条件查询字典类型列表（支持分页）")
    public Response<TablePage<DictTypeVO>> listPage(DictTypeDTO dictTypeDTO, PageQuery pageQuery) {
        TablePage<DictTypeVO> tablePage = dictTypeService.listPage(dictTypeDTO, pageQuery);
        return HttpResult.ok(tablePage);
    }

    @PostMapping
    @Operation(summary = "字典类型列表新增", description = "新增字典类型列表")
    @PreventRepeatSubmit
    public Response<Boolean> addDictType(@Validated(RestGroup.AddGroup.class) @RequestBody DictTypeDTO dictTypeDTO) {
        if (dictTypeService.checkDictCodeUnique(dictTypeDTO)) {
            return HttpResult.failMessage("新增字典类型编码「" + dictTypeDTO.getDictCode() + "」失败，字典字典类型编码已存在");
        }

        return HttpResult.ok(dictTypeService.insertOne(dictTypeDTO));
    }

    @PutMapping
    @Operation(summary = "字典类型列表修改", description = "修改字典类型列表")
    @PreventRepeatSubmit
    public Response<Boolean> editDictType(@Validated(RestGroup.EditGroup.class) @RequestBody DictTypeDTO dictTypeDTO) {
        if (dictTypeService.checkDictCodeUnique(dictTypeDTO)) {
            return HttpResult.failMessage("修改字典类型编码「" + dictTypeDTO.getDictCode() + "」失败，字典字典类型编码已存在");
        }

        return HttpResult.ok(dictTypeService.updateOne(dictTypeDTO));
    }

    /**
     * 客户端删除
     */
    @DeleteMapping("/{ids}")
    @Operation(summary = "字典类型列表删除", description = "通过主键批量删除字典类型列表")
    @PreventRepeatSubmit
    public Response<Boolean> removeBatchDictType(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        List<Long> idList = List.of(ids);
        List<DictType> dictTypeList = dictDataService.checkDictTypeExitDataAndGet(idList);

        if (!dictTypeList.isEmpty()) {
            String dictNames = dictTypeList.stream()
                    .map(DictType::getDictName)
                    .distinct()
                    .collect(Collectors.joining(","));

            return HttpResult.failMessage(dictNames + " 已分配字典数据，不允许删除");
        }
        return HttpResult.ok(dictTypeService.removeBatch(idList));
    }

    @PostMapping("/export")
    @Operation(summary = "字典类型导出", description = "导出字典类型")
    public void export(@RequestBody DictTypeDTO dictTypeDTO, HttpServletResponse response) {
        List<DictTypeVO> dictTypeVOList = dictTypeService.listAll(dictTypeDTO);
        ExcelHelper.exportExcel(dictTypeVOList, "字典类型数据", DictTypeVO.class, response);
    }
}
