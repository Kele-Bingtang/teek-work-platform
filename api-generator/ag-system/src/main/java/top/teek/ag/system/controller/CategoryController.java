package top.teek.ag.system.controller;

import top.teek.ag.system.model.dto.CategoryDTO;
import top.teek.ag.system.model.vo.CategoryVO;
import top.teek.ag.system.permission.annotation.ProjectAuthorize;
import top.teek.ag.system.service.CategoryService;
import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.core.validate.RestGroup;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/6/25 21:42:09
 * @note
 */
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    @Operation(summary = "目录列表查询", description = "通过条件查询目录列表")
    public Response<List<CategoryVO>> list(@Validated(RestGroup.QueryGroup.class) CategoryDTO categoryDTO) {
        List<CategoryVO> categoryVOList = categoryService.listAll(categoryDTO);
        return HttpResult.ok(categoryVOList);
    }

    @GetMapping("/listPage")
    @Operation(summary = "目录列表查询（分页）", description = "通过条件查询目录列表（分页）")
    @ProjectAuthorize(value = "#categoryDTO.getProjectId()", checkRead = true)
    public Response<TablePage<CategoryVO>> listPage(@Validated(RestGroup.QueryGroup.class) CategoryDTO categoryDTO, PageQuery pageQuery) {
        TablePage<CategoryVO> categoryVOTablePage = categoryService.listPage(categoryDTO, pageQuery);
        return HttpResult.ok(categoryVOTablePage);
    }

    @PostMapping
    @Operation(summary = "目录新增", description = "新增目录")
    @ProjectAuthorize(value = "#categoryDTO.getProjectId()", checkReadAndWrite = true)
    public Response<Boolean> addCategory(@Validated(RestGroup.AddGroup.class) @RequestBody CategoryDTO categoryDTO) {
        if (categoryService.checkCategoryCodeUnique(categoryDTO)) {
            return HttpResult.failMessage("新增目录「" + categoryDTO.getCategoryName() + "」失败，目录编码已存在");
        }

        boolean addCategory = categoryService.addCategory(categoryDTO);
        return HttpResult.okOrFail(addCategory);
    }

    @PutMapping
    @Operation(summary = "目录修改", description = "修改目录")
    @ProjectAuthorize(value = "#categoryDTO.getProjectId()", checkReadAndWrite = true)
    public Response<Boolean> editCategory(@Validated(RestGroup.EditGroup.class) @RequestBody CategoryDTO categoryDTO) {
        if (categoryService.checkCategoryCodeUnique(categoryDTO)) {
            return HttpResult.failMessage("新增目录「" + categoryDTO.getCategoryName() + "」失败，目录编码已存在");
        }

        boolean editCategory = categoryService.editCategory(categoryDTO);
        return HttpResult.okOrFail(editCategory);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "目录删除", description = "删除目录")
    public Response<Boolean> removeCategory(@PathVariable String categoryId) {
        boolean removeCategory = categoryService.removeCategory(categoryId);
        return HttpResult.okOrFail(removeCategory);
    }

}
