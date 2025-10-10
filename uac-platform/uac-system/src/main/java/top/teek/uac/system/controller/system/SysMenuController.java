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
import top.teek.uac.system.model.dto.SysMenuDTO;
import top.teek.uac.system.model.vo.SysMenuVO;
import top.teek.uac.system.model.vo.router.RouterVO;
import top.teek.uac.system.service.RoleMenuLinkService;
import top.teek.uac.system.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Teeker
 * @date 2023/12/4 16:41
 * @note
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/menu")
public class SysMenuController {
    
    private final SysMenuService sysMenuService;
    private final RoleMenuLinkService roleMenuLinkService;

    @GetMapping("/listRoutes/{appId}")
    @Operation(summary = "路由列表查询", description = "查询前端需要的路由列表")
    public Response<List<RouterVO>> listRoutes(@PathVariable String appId) {
        List<RouterVO> routerVOList = sysMenuService.listRoutes(appId);
        return HttpResult.ok(routerVOList);
    }
     

    @GetMapping("/list")
    @Operation(summary = "菜单列表查询", description = "通过查询条件查询菜单列表")
    @PreAuthorize("hasAuthority('system:menu:list')")
    public Response<List<SysMenuVO>> list(SysMenuDTO sysMenuDTO) {
        List<SysMenuVO> sysMenuVOList = sysMenuService.listAll(sysMenuDTO);
        return HttpResult.ok(sysMenuVOList);
    }

    @GetMapping("/listPage")
    @Operation(summary = "菜单列表查询", description = "通过查询条件查询菜单列表（支持分页）")
    @PreAuthorize("hasAuthority('system:menu:list')")
    public Response<TablePage<SysMenuVO>> listPage(SysMenuDTO sysMenuDTO, PageQuery pageQuery) {
        TablePage<SysMenuVO> tablePage = sysMenuService.listPage(sysMenuDTO, pageQuery);
        return HttpResult.ok(tablePage);
    }

    @GetMapping("/treeSelect")
    @Operation(summary = "菜单下拉值查询", description = "通过查询条件查询菜单下拉值（下拉框查询使用）")
    @PreAuthorize("hasAuthority('system:menu:query')")
    public Response<List<Tree<String>>> listMenuTreeSelect(@Validated(RestGroup.QueryGroup.class) SysMenuDTO sysMenuDTO) {
        List<Tree<String>> menuTreeList = sysMenuService.listMenuTreeSelect(sysMenuDTO);
        return HttpResult.ok(menuTreeList);
    }

    @GetMapping("/treeTable")
    @Operation(summary = "菜单树表查询", description = "通过查询条件查询菜单树表")
    @PreAuthorize("hasAuthority('system:menu:list')")
    public Response<List<SysMenuVO>> listMenuTreeTable(@Validated(RestGroup.QueryGroup.class) SysMenuDTO sysMenuDTO) {
        List<SysMenuVO> treeTable = sysMenuService.listMenuTreeTable(sysMenuDTO);
        return HttpResult.ok(treeTable);
    }
    
    @GetMapping("/listMenuIdsByRoleId/{appId}/{roleId}")
    @Operation(summary = "菜单列表查询", description = "通过角色 ID 查询菜单 ID 列表")
    @PreAuthorize("hasAuthority('system:menu:query')")
    public Response<List<String>> listMenuIdsByRoleId(@PathVariable String appId, @PathVariable String roleId) {
        List<String> menuIds = roleMenuLinkService.listMenuIdsByRoleId(roleId, appId, null);
        return HttpResult.ok(menuIds);
    }

    @GetMapping("/listMenuListByRoleId/{appId}/{roleId}")
    @Operation(summary = "菜单列表查询", description = "通过角色 ID 查询菜单 ID 列表")
    @PreAuthorize("hasAuthority('system:menu:query')")
    public Response<List<Tree<String>>> listMenuListByRoleId(@PathVariable String appId, @PathVariable String roleId) {
        List<Tree<String>> sysMenuVOList = sysMenuService.listMenuListByRoleId(roleId, appId);
        return HttpResult.ok(sysMenuVOList);
    }

    @PostMapping
    @Operation(summary = "菜单新增", description = "新增菜单")
    @OperateLog(title = "菜单管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('system:menu:add')")
    @PreventRepeatSubmit
    public Response<Boolean> addMenu(@Validated(RestGroup.AddGroup.class) @RequestBody SysMenuDTO sysMenuDTO) {
        if (sysMenuService.checkMenuCodeUnique(sysMenuDTO)) {
            return HttpResult.failMessage("新增菜单「" + sysMenuDTO.getMenuName() + "」失败，菜单名称「" + sysMenuDTO.getMenuCode() + "」已存在");
        }

        if (sysMenuService.checkMenuNameUnique(sysMenuDTO)) {
            return HttpResult.failMessage("新增菜单「" + sysMenuDTO.getMenuName() + "」失败，菜单名称已存在");
        }

        return HttpResult.ok(sysMenuService.addMenu(sysMenuDTO));
    }

    @PutMapping
    @Operation(summary = "菜单修改", description = "修改菜单")
    @OperateLog(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('system:menu:edit')")
    @PreventRepeatSubmit
    public Response<Boolean> editMenu(@Validated(RestGroup.EditGroup.class) @RequestBody SysMenuDTO sysMenuDTO) {

        if (sysMenuDTO.getParentId().equals(sysMenuDTO.getMenuId())) {
            return HttpResult.failMessage("修改菜单「" + sysMenuDTO.getMenuName() + "」失败，上级菜单不能是自己");
        }

        if (sysMenuService.checkMenuCodeUnique(sysMenuDTO)) {
            return HttpResult.failMessage("修改菜单「" + sysMenuDTO.getMenuName() + "」失败，菜单名称「" + sysMenuDTO.getMenuCode() + "」已存在");
        }
        
        if (sysMenuService.checkMenuNameUnique(sysMenuDTO)) {
            return HttpResult.failMessage("修改菜单「" + sysMenuDTO.getMenuName() + "」失败，菜单名称已存在");
        }

        return HttpResult.ok(sysMenuService.editMenu(sysMenuDTO));
    }

    @DeleteMapping("/{id}/{menuId}")
    @Operation(summary = "菜单删除", description = "通过主键删除菜单")
    @OperateLog(title = "菜单管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('system:menu:remove')")
    @PreventRepeatSubmit
    public Response<Boolean> removeMenu(@PathVariable Long id, @PathVariable String menuId) {
        if (sysMenuService.hasChild(menuId)) {
            return HttpResult.failMessage("存在下级菜单，不允许删除");
        }

        if (sysMenuService.checkMenuExistRole(menuId)) {
            return HttpResult.failMessage("菜单存在角色，不允许删除");
        }

        return HttpResult.ok(sysMenuService.removeMenu(id));
    }

    @PostMapping("/export")
    @Operation(summary = "菜单数据导出", description = "导出菜单数据")
    @OperateLog(title = "菜单管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("hasAuthority('system:menu:export')")
    public void export(@RequestBody SysMenuDTO sysMenuDTO, HttpServletResponse response) {
        List<SysMenuVO> sysMenuVOList = sysMenuService.listAll(sysMenuDTO);
        ExcelHelper.exportExcel(sysMenuVOList, "菜单数据", SysMenuVO.class, response);
    }
    
}
