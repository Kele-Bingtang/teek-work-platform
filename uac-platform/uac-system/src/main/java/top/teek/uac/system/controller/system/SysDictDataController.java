package top.teek.uac.system.controller.system;

import cn.hutool.core.lang.tree.Tree;
import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.core.validate.RestGroup;
import top.teek.excel.helper.ExcelHelper;
import top.teek.idempotent.annotation.PreventRepeatSubmit;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.core.log.annotation.OperateLog;
import top.teek.uac.core.log.enums.BusinessType;
import top.teek.uac.system.model.dto.SysDictDataDTO;
import top.teek.uac.system.model.vo.SysDictDataVO;
import top.teek.uac.system.service.SysDictDataService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/system/dict/data")
public class SysDictDataController {
    private final SysDictDataService sysDictDataService;

    @GetMapping("/list")
    @Operation(summary = "字典数据列表查询", description = "通过条件查询字典数据列表")
    public Response<List<SysDictDataVO>> list(SysDictDataDTO sysDictDataDTO) {
        List<SysDictDataVO> sysDictDataVOList = sysDictDataService.listAll(sysDictDataDTO);
        return HttpResult.ok(sysDictDataVOList);
    }

    @GetMapping("/listPage")
    @Operation(summary = "字典数据列表查询", description = "通过条件查询字典数据列表（支持分页）")
    @PreAuthorize("hasAuthority('system:dict:list')")
    public Response<TablePage<SysDictDataVO>> listPage(SysDictDataDTO sysDictDataDTO, PageQuery pageQuery) {
        TablePage<SysDictDataVO> tablePage = sysDictDataService.listPage(sysDictDataDTO, pageQuery);
        return HttpResult.ok(tablePage);
    }

    @GetMapping("/treeList")
    @Operation(summary = "字典数据树形结构查询", description = "通过条件查询字典数据树形结构")
    @PreAuthorize("hasAuthority('system:dict:query')")
    public Response<List<Tree<String>>> listDataTreeList(SysDictDataDTO sysDictDataDTO) {
        List<Tree<String>> dataTreeList = sysDictDataService.listDataTreeList(sysDictDataDTO);
        return HttpResult.ok(dataTreeList);
    }

    @GetMapping("/treeTable")
    @Operation(summary = "字典数据树形表格查询", description = "通过条件查询字典数据树形表格")
    @PreAuthorize("hasAuthority('system:dict:list')")
    public Response<List<SysDictDataVO>> listDataTreeTable(SysDictDataDTO sysDictDataDTO) {
        List<SysDictDataVO> dataTreeTable = sysDictDataService.listDataTreeTable(sysDictDataDTO);
        return HttpResult.ok(dataTreeTable);
    }

    @PostMapping
    @Operation(summary = "字典数据新增", description = "新增字典数据")
    @OperateLog(title = "字典数据管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('system:dict:add')")
    @PreventRepeatSubmit
    public Response<Boolean> addDictData(@Validated(RestGroup.AddGroup.class) @RequestBody SysDictDataDTO sysDictDataDTO) {
        if (sysDictDataService.checkDictDataValueUnique(sysDictDataDTO)) {
            return HttpResult.failMessage("新增字典数据「" + sysDictDataDTO.getDictLabel() + "」失败，字典数据值「" + sysDictDataDTO.getDictValue() + "」已存在");
        }

        return HttpResult.ok(sysDictDataService.addDictData(sysDictDataDTO));
    }

    @PutMapping
    @Operation(summary = "字典数据修改", description = "修改字典数据")
    @OperateLog(title = "字典数据管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('system:dict:edit')")
    @PreventRepeatSubmit
    public Response<Boolean> editDictData(@Validated(RestGroup.EditGroup.class) @RequestBody SysDictDataDTO sysDictDataDTO) {
        if (Objects.nonNull(sysDictDataDTO.getParentId()) && sysDictDataDTO.getParentId().equals(sysDictDataDTO.getDataId())) {
            return HttpResult.failMessage("修改字典数据「" + sysDictDataDTO.getDictLabel() + "」失败，上级字典数据不能是自己");
        }
        if (sysDictDataService.checkDictDataValueUnique(sysDictDataDTO)) {
            return HttpResult.failMessage("修改字典数据「" + sysDictDataDTO.getDictLabel() + "」失败，字典数据值「" + sysDictDataDTO.getDictValue() + "」已存在");
        }

        return HttpResult.ok(sysDictDataService.editDictData(sysDictDataDTO));
    }

    @DeleteMapping("/{ids}")
    @Operation(summary = "字典数据删除", description = "通过主键批量删除字典数据")
    @OperateLog(title = "字典数据管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('system:dict:remove')")
    @PreventRepeatSubmit
    public Response<Boolean> removeBatch(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return HttpResult.ok(sysDictDataService.removeBatch(List.of(ids)));
    }

    @PostMapping("/export")
    @Operation(summary = "字典数据导出", description = "导出字典数据")
    @OperateLog(title = "字典数据管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("hasAuthority('system:dict:export')")
    public void export(@RequestBody SysDictDataDTO sysDictDataDTO, HttpServletResponse response) {
        List<SysDictDataVO> sysDictDataVOList = sysDictDataService.listAll(sysDictDataDTO);
        ExcelHelper.exportExcel(sysDictDataVOList, "字典数据", SysDictDataVO.class, response);
    }
}
