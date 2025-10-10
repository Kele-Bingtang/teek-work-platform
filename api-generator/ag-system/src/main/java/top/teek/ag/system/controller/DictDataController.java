package top.teek.ag.system.controller;

import cn.hutool.core.lang.tree.Tree;
import top.teek.ag.system.model.dto.DictDataDTO;
import top.teek.ag.system.model.vo.DictDataVO;
import top.teek.ag.system.service.DictDataService;
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
import java.util.Objects;

/**
 * @author Teeker
 * @date 2023/12/4 16:15
 * @note
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/dict/data")
public class DictDataController {
    private final DictDataService dictDataService;

    @GetMapping("/list")
    @Operation(summary = "字典数据列表查询", description = "通过条件查询字典数据列表")
    public Response<List<DictDataVO>> list(DictDataDTO dictDataDTO) {
        List<DictDataVO> dictDataVOList = dictDataService.listAll(dictDataDTO);
        return HttpResult.ok(dictDataVOList);
    }

    @GetMapping("/listPage")
    @Operation(summary = "字典数据列表查询", description = "通过条件查询字典数据列表（支持分页）")
    public Response<TablePage<DictDataVO>> listPage(DictDataDTO dictDataDTO, PageQuery pageQuery) {
        TablePage<DictDataVO> tablePage = dictDataService.listPage(dictDataDTO, pageQuery);
        return HttpResult.ok(tablePage);
    }

    @GetMapping("/treeList")
    @Operation(summary = "字典数据树形结构查询", description = "通过条件查询字典数据树形结构")
    public Response<List<Tree<String>>> listDataTreeList(DictDataDTO dictDataDTO) {
        List<Tree<String>> dataTreeList = dictDataService.listDataTreeList(dictDataDTO);
        return HttpResult.ok(dataTreeList);
    }

    @GetMapping("/treeTable")
    @Operation(summary = "字典数据树形表格查询", description = "通过条件查询字典数据树形表格")
    public Response<List<DictDataVO>> listDataTreeTable(DictDataDTO dictDataDTO) {
        List<DictDataVO> dataTreeTable = dictDataService.listDataTreeTable(dictDataDTO);
        return HttpResult.ok(dataTreeTable);
    }

    @PostMapping
    @Operation(summary = "字典数据新增", description = "新增字典数据")
    @PreventRepeatSubmit
    public Response<Boolean> addDictData(@Validated(RestGroup.AddGroup.class) @RequestBody DictDataDTO dictDataDTO) {
        if (dictDataService.checkDictDataValueUnique(dictDataDTO)) {
            return HttpResult.failMessage("新增字典数据「" + dictDataDTO.getDictLabel() + "」失败，字典数据值「" + dictDataDTO.getDictValue() + "」已存在");
        }

        return HttpResult.ok(dictDataService.addDictType(dictDataDTO));
    }

    @PutMapping
    @Operation(summary = "字典数据修改", description = "修改字典数据")
    @PreventRepeatSubmit
    public Response<Boolean> editDictData(@Validated(RestGroup.EditGroup.class) @RequestBody DictDataDTO dictDataDTO) {
        if (Objects.nonNull(dictDataDTO.getParentId()) && dictDataDTO.getParentId().equals(dictDataDTO.getDataId())) {
            return HttpResult.failMessage("修改字典数据「" + dictDataDTO.getDictLabel() + "」失败，上级字典数据不能是自己");
        }
        if (dictDataService.checkDictDataValueUnique(dictDataDTO)) {
            return HttpResult.failMessage("修改字典数据「" + dictDataDTO.getDictLabel() + "」失败，字典数据值「" + dictDataDTO.getDictValue() + "」已存在");
        }

        return HttpResult.ok(dictDataService.editDictType(dictDataDTO));
    }

    @DeleteMapping("/{ids}")
    @Operation(summary = "字典数据删除", description = "通过主键批量删除字典数据")
    @PreventRepeatSubmit
    public Response<Boolean> removeBatchDictData(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return HttpResult.ok(dictDataService.removeBatch(List.of(ids)));
    }

    @PostMapping("/export")
    @Operation(summary = "字典数据导出", description = "导出字典数据")
    public void export(@RequestBody DictDataDTO dictDataDTO, HttpServletResponse response) {
        List<DictDataVO> dictDataVOList = dictDataService.listAll(dictDataDTO);
        ExcelHelper.exportExcel(dictDataVOList, "字典数据", DictDataVO.class, response);
    }
}
