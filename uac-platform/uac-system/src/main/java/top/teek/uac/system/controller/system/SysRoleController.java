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
import top.teek.uac.system.model.dto.SysRoleDTO;
import top.teek.uac.system.model.dto.UserRoleLinkDTO;
import top.teek.uac.system.model.dto.link.RoleLinkDeptDTO;
import top.teek.uac.system.model.dto.link.RoleLinkInfoDTO;
import top.teek.uac.system.model.dto.link.RoleLinkUserDTO;
import top.teek.uac.system.model.dto.link.RoleLinkUserGroupDTO;
import top.teek.uac.system.model.vo.SysRoleVO;
import top.teek.uac.system.model.vo.link.RoleBindSelectVO;
import top.teek.uac.system.model.vo.link.RoleLinkVO;
import top.teek.uac.system.service.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.teek.uac.system.service.*;

import java.util.List;

/**
 * @author Teeker
 * @date 2023/12/4 17:39
 * @note
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/role")
public class SysRoleController {

    private final SysRoleService sysRoleService;
    private final UserRoleLinkService userRoleLinkService;
    private final UserGroupRoleLinkService userGroupRoleLinkService;
    private final RoleMenuLinkService roleMenuLinkService;
    private final RoleDeptLinkService roleDeptLinkService;

    @GetMapping("/list")
    @Operation(summary = "角色列表查询", description = "通过条件查询角色列表")
    @PreAuthorize("hasAuthority('system:role:list')")
    public Response<List<SysRoleVO>> list(SysRoleDTO sysRoleDTO) {
        List<SysRoleVO> sysRoleVOList = sysRoleService.listAll(sysRoleDTO);
        return HttpResult.ok(sysRoleVOList);
    }

    @GetMapping("/listPage")
    @Operation(summary = "角色列表查询", description = "通过条件查询角色列表（支持分页）")
    @PreAuthorize("hasAuthority('system:role:list')")
    public Response<TablePage<SysRoleVO>> listPage(SysRoleDTO sysRoleDTO, PageQuery pageQuery) {
        TablePage<SysRoleVO> tablePage = sysRoleService.listPage(sysRoleDTO, pageQuery);
        return HttpResult.ok(tablePage);
    }

    @PostMapping
    @Operation(summary = "角色列表新增", description = "新增角色")
    @OperateLog(title = "角色管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('system:role:add')")
    @PreventRepeatSubmit
    public Response<Boolean> addRole(@Validated(RestGroup.AddGroup.class) @RequestBody SysRoleDTO sysRoleDTO) {
        if (sysRoleService.checkRoleCodeUnique(sysRoleDTO)) {
            return HttpResult.failMessage("新增角色「" + sysRoleDTO.getRoleName() + "」失败，角色编码「" + sysRoleDTO.getRoleCode() + "」已存在");
        }
        if (sysRoleService.checkRoleNameUnique(sysRoleDTO)) {
            return HttpResult.failMessage("新增角色「" + sysRoleDTO.getRoleName() + "」失败，角色名称已存在");
        }

        return HttpResult.ok(sysRoleService.addRole(sysRoleDTO));
    }

    @PutMapping
    @Operation(summary = "角色列表修改", description = "修改角色")
    @OperateLog(title = "角色管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('system:role:edit')")
    @PreventRepeatSubmit
    public Response<Boolean> editRole(@Validated(RestGroup.EditGroup.class) @RequestBody SysRoleDTO sysRoleDTO) {
        if (sysRoleService.checkRoleCodeUnique(sysRoleDTO)) {
            return HttpResult.failMessage("修改角色「" + sysRoleDTO.getRoleName() + "」失败，角色编码「" + sysRoleDTO.getRoleCode() + "」已存在");
        }
        if (sysRoleService.checkRoleNameUnique(sysRoleDTO)) {
            return HttpResult.failMessage("修改角色「" + sysRoleDTO.getRoleName() + "」失败，角色名称已存在");
        }

        return HttpResult.ok(sysRoleService.editRole(sysRoleDTO));
    }

    @DeleteMapping("/{ids}")
    @Operation(summary = "角色列表删除", description = "通过主键批量删除角色列表")
    @OperateLog(title = "角色管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('system:role:remove')")
    @PreventRepeatSubmit
    public Response<Boolean> removeBatch(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids, @RequestBody List<String> roleIds) {
        return HttpResult.ok(sysRoleService.removeBatch(List.of(ids), roleIds));
    }

    @GetMapping("/listRoleLinkByUserId/{appId}/{userId}")
    @Operation(summary = "角色列表查询", description = "查询某个用户所在的角色列表")
    @PreAuthorize("hasAuthority('system:role:query')")
    public Response<List<RoleLinkVO>> listRoleListByUserId(@PathVariable String appId, @PathVariable String userId) {
        List<RoleLinkVO> roleLinkVOS = userRoleLinkService.listRoleLinkByUserId(appId, userId);
        return HttpResult.ok(roleLinkVOS);
    }

    @GetMapping("listRoleLinkByGroupId/{userGroupId}")
    @Operation(summary = "角色列表查询", description = "通过用户组 ID 查询角色列表")
    @PreAuthorize("hasAuthority('system:role:query')")
    public Response<TablePage<RoleLinkVO>> listRoleLinkByGroupId(@PathVariable String userGroupId, RoleLinkInfoDTO roleLinkInfoDTO, PageQuery pageQuery) {
        TablePage<RoleLinkVO> tablePage = userGroupRoleLinkService.listRoleLinkByGroupId(userGroupId, roleLinkInfoDTO, pageQuery);
        return HttpResult.ok(tablePage);
    }

    @GetMapping("listWithDisabledByUserId/{appId}/{userId}")
    @Operation(summary = "角色列表查询", description = "查询所有角色列表，如果角色绑定了用户，则 disabled 属性为 true")
    @PreAuthorize("hasAuthority('system:role:query')")
    public Response<List<RoleBindSelectVO>> listWithDisabledByUserId(@PathVariable String appId, @PathVariable String userId) {
        List<RoleBindSelectVO> roleBindSelectVOList = userRoleLinkService.listWithDisabledByUserId(appId, userId);
        return HttpResult.ok(roleBindSelectVOList);
    }

    @GetMapping("/listWithDisabledByGroupId/{userGroupId}")
    @Operation(summary = "角色列表查询", description = "查询所有角色列表（查询所有角色列表，如果角色绑定了用户组，则 disabled 属性为 true）")
    @PreAuthorize("hasAuthority('system:role:query')")
    public Response<List<RoleBindSelectVO>> listWithDisabledByGroupId(@PathVariable String userGroupId) {
        List<RoleBindSelectVO> roleBindSelectVOList = userGroupRoleLinkService.listWithDisabledByGroupId(userGroupId);
        return HttpResult.ok(roleBindSelectVOList);
    }

    @PostMapping("/addUserGroupsToRole")
    @Operation(summary = "添加角色到用户组", description = "添加角色到用户组（多个用户组）")
    @OperateLog(title = "用户组角色关联管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('system:role:add')")
    public Response<Boolean> addUserGroupsToRole(@Validated(RestGroup.AddGroup.class) @RequestBody RoleLinkUserGroupDTO roleLinkUserGroupDTO) {
        if (userGroupRoleLinkService.checkRoleExistUserGroups(roleLinkUserGroupDTO)) {
            return HttpResult.failMessage("添加角色到用户组失败，用户组已存在于角色中");
        }
        boolean result = userGroupRoleLinkService.addUserGroupsToRole(roleLinkUserGroupDTO);
        return HttpResult.ok(result);
    }

    @PostMapping("/addUsersToRole")
    @Operation(summary = "添加用户到角色", description = "添加用户到角色（多个用户）")
    @OperateLog(title = "用户角色关联管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('system:role:add')")
    public Response<Boolean> addUsersToRole(@Validated(RestGroup.AddGroup.class) @RequestBody RoleLinkUserDTO roleLinkUserDTO) {
        if (userRoleLinkService.checkRoleExistUser(roleLinkUserDTO)) {
            return HttpResult.failMessage("添加用户到角色失败，用户已存在于角色中");
        }
        boolean result = userRoleLinkService.addUsersToRole(roleLinkUserDTO);
        return HttpResult.ok(result);
    }

    @DeleteMapping("/removeUserFromRole/{ids}")
    @Operation(summary = "移出角色", description = "将用户移出角色")
    @OperateLog(title = "用户组角色关联管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('system:role:remove')")
    public Response<Boolean> removeUserFromRole(@PathVariable Long[] ids) {
        boolean result = userRoleLinkService.removeUserFromRole(List.of(ids));
        return HttpResult.ok(result);
    }

    @DeleteMapping("/removeUserGroupFromRole/{ids}")
    @Operation(summary = "移出角色", description = "将用户组移出角色")
    @OperateLog(title = "用户组角色关联管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('system:role:remove')")
    public Response<Boolean> removeUserGroupFromRole(@PathVariable Long[] ids) {
        boolean result = userGroupRoleLinkService.removeUserGroupFromRole(List.of(ids));
        return HttpResult.ok(result);
    }

    @PutMapping("/editUserRoleLink")
    @Operation(summary = "用户关联角色信息修改", description = "修改用户组和角色关联信息")
    @OperateLog(title = "用户角色关联管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('system:role:edit')")
    public Response<Boolean> editUserRoleLink(@Validated(RestGroup.EditGroup.class) @RequestBody UserRoleLinkDTO userRoleLinkDTO) {
        return HttpResult.ok(userRoleLinkService.updateOne(userRoleLinkDTO));
    }
    
    @PostMapping("/addMenusToRole")
    @Operation(summary = "添加菜单到角色", description = "添加菜单到角色")
    @OperateLog(title = "角色菜单关联管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('system:role:add')")
    public Response<Boolean> addMenusToRole(@RequestBody SysRoleDTO sysRoleDTO) {
        return HttpResult.ok(roleMenuLinkService.addMenusToRole(sysRoleDTO, true));
    }

    @PostMapping("/addDeptsToRole")
    @Operation(summary = "添加部门到角色", description = "添加部门到角色")
    @OperateLog(title = "角色部门关联管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('system:role:add')")
    public Response<Boolean> addDeptsToRole(@RequestBody RoleLinkDeptDTO roleLinkDeptDTO) {
        return HttpResult.ok(roleDeptLinkService.addDeptsToRole(roleLinkDeptDTO, true));
    }

    @PostMapping("/export")
    @Operation(summary = "角色数据导出", description = "导出角色数据")
    @OperateLog(title = "角色管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("hasAuthority('system:role:export')")
    public void export(@RequestBody SysRoleDTO sysRoleDTO, HttpServletResponse response) {
        List<SysRoleVO> sysRoleVOList = sysRoleService.listAll(sysRoleDTO);
        ExcelHelper.exportExcel(sysRoleVOList, "角色数据", SysRoleVO.class, response);
    }
}
