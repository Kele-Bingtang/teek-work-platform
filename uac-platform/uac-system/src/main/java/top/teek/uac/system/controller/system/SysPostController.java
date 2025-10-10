package top.teek.uac.system.controller.system;

import top.teek.core.constants.ColumnConstant;
import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.core.validate.RestGroup;
import top.teek.excel.helper.ExcelHelper;
import top.teek.idempotent.annotation.PreventRepeatSubmit;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.core.log.annotation.OperateLog;
import top.teek.uac.core.log.enums.BusinessType;
import top.teek.uac.system.model.dto.SysPostDTO;
import top.teek.uac.system.model.vo.SysPostVO;
import top.teek.uac.system.model.vo.extra.UserSelectPostVo;
import top.teek.uac.system.service.SysPostService;
import top.teek.uac.system.service.UserPostLinkService;
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
 * @date 2023/12/4 17:33
 * @note
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/post")
public class SysPostController {
    private final SysPostService sysPostService;
    private final UserPostLinkService userPostLinkService;

    @GetMapping("/list")
    @Operation(summary = "岗位列表查询", description = "通过查询条件查询岗位列表")
    @PreAuthorize("hasAuthority('system:post:list')")
    public Response<List<SysPostVO>> list(SysPostDTO sysPostDTO) {
        List<SysPostVO> sysPostVOList = sysPostService.listAll(sysPostDTO);
        return HttpResult.ok(sysPostVOList);
    }

    @GetMapping("/listPage")
    @Operation(summary = "岗位列表查询", description = "通过查询条件查询岗位列表（支持分页）")
    @PreAuthorize("hasAuthority('system:post:list')")
    public Response<TablePage<SysPostVO>> listPage(SysPostDTO sysPostDTO, PageQuery pageQuery) {
        TablePage<SysPostVO> tablePage = sysPostService.listPage(sysPostDTO, pageQuery);
        return HttpResult.ok(tablePage);
    }

    @GetMapping("/userSelectPostList/{userId}")
    @Operation(summary = "角色岗位列表查询", description = "查询岗位列表和已选择的岗位列表")
    @PreAuthorize("hasAuthority('system:post:query')")
    public Response<UserSelectPostVo> userSelectPostList(@PathVariable String userId) {
        UserSelectPostVo userSelectPostVo = sysPostService.userSelectPostList(userId);
        return HttpResult.ok(userSelectPostVo);
    }

    @PostMapping
    @Operation(summary = "新增岗位", description = "新增岗位")
    @OperateLog(title = "新增管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('system:post:add')")
    @PreventRepeatSubmit
    public Response<Boolean> addPost(@Validated(RestGroup.AddGroup.class) @RequestBody SysPostDTO sysPostDTO) {
        if (sysPostService.checkPostCodeUnique(sysPostDTO)) {
            return HttpResult.failMessage("新增岗位「" + sysPostDTO.getPostName() + "」失败，岗位编码「" + sysPostDTO.getPostCode() + "」已存在");
        }
        if (sysPostService.checkPostNameUnique(sysPostDTO)) {
            return HttpResult.failMessage("新增岗位「" + sysPostDTO.getPostName() + "」失败，岗位名称已存在");
        }

        return HttpResult.ok(sysPostService.addPost(sysPostDTO));
    }

    @PutMapping
    @Operation(summary = "修改岗位", description = "修改岗位")
    @OperateLog(title = "新增管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('system:post:edit')")
    @PreventRepeatSubmit
    public Response<Boolean> editPost(@Validated(RestGroup.EditGroup.class) @RequestBody SysPostDTO sysPostDTO) {
        if (sysPostService.checkPostCodeUnique(sysPostDTO)) {
            return HttpResult.failMessage("修改岗位「" + sysPostDTO.getPostName() + "」失败，岗位编码「" + sysPostDTO.getPostCode() + "」已存在");
        }
        if (sysPostService.checkPostNameUnique(sysPostDTO)) {
            return HttpResult.failMessage("修改岗位「" + sysPostDTO.getPostName() + "」失败，岗位名称已存在");
        }
        if (ColumnConstant.STATUS_EXCEPTION.equals(sysPostDTO.getStatus())
                && sysPostService.checkPostExitUser(sysPostDTO)) {
            return HttpResult.failMessage("该岗位下存在已分配用户，不能禁用!");
        }

        return HttpResult.ok(sysPostService.editPost(sysPostDTO));
    }

    @DeleteMapping("/{ids}")
    @Operation(summary = "删除岗位", description = "通过主键批量删除岗位")
    @OperateLog(title = "新增管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('system:post:remove')")
    @PreventRepeatSubmit
    public Response<Boolean> removeBatch(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids, @RequestBody List<String> postIds) {
        if (userPostLinkService.checkPostExistUser(postIds)) {
            return HttpResult.failMessage("该岗位已绑定用户，不允许删除");
        }
        return HttpResult.ok(sysPostService.removeBatch(List.of(ids)));
    }

    @PostMapping("/export")
    @Operation(summary = "岗位数据导出", description = "导出岗位数据")
    @OperateLog(title = "岗位数据管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("hasAuthority('system:post:export')")
    public void export(@RequestBody SysPostDTO sysPostDTO, HttpServletResponse response) {
        List<SysPostVO> sysPostVOList = sysPostService.listAll(sysPostDTO);
        ExcelHelper.exportExcel(sysPostVOList, "岗位数据", SysPostVO.class, response);
    }
}
