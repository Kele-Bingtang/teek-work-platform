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
import top.teek.uac.system.model.dto.SysUserGroupDTO;
import top.teek.uac.system.model.dto.UserGroupLinkDTO;
import top.teek.uac.system.model.dto.link.UserGroupLinkInfoDTO;
import top.teek.uac.system.model.dto.link.UserGroupLinkRoleDTO;
import top.teek.uac.system.model.dto.link.UserGroupLinkUserDTO;
import top.teek.uac.system.model.vo.SysUserGroupVO;
import top.teek.uac.system.model.vo.link.UserGroupBindSelectVO;
import top.teek.uac.system.model.vo.link.UserGroupLinkVO;
import top.teek.uac.system.service.SysUserGroupService;
import top.teek.uac.system.service.UserGroupLinkService;
import top.teek.uac.system.service.UserGroupRoleLinkService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/3/12 23:59
 * @note
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/userGroup")
public class SysUserGroupController {

    private final SysUserGroupService sysUserGroupService;
    private final UserGroupLinkService userGroupLinkService;
    private final UserGroupRoleLinkService userGroupRoleLinkService;

    @GetMapping("/list")
    @Operation(summary = "用户组列表查询", description = "通过主键查询用户组列表")
    @PreAuthorize("hasAuthority('system:userGroup:list')")
    public Response<List<SysUserGroupVO>> list(SysUserGroupDTO sysUserGroupDTO) {
        List<SysUserGroupVO> sysUserGroupVOList = sysUserGroupService.listAll(sysUserGroupDTO);
        return HttpResult.ok(sysUserGroupVOList);
    }

    @GetMapping("/listPage")
    @Operation(summary = "用户组列表查询", description = "通过主键查询用户组列表（分页）")
    @PreAuthorize("hasAuthority('system:userGroup:list')")
    public Response<TablePage<SysUserGroupVO>> listPage(SysUserGroupDTO sysUserGroupDTO, PageQuery pageQuery) {
        TablePage<SysUserGroupVO> tablePage = sysUserGroupService.listPage(sysUserGroupDTO, pageQuery);
        return HttpResult.ok(tablePage);
    }

    @GetMapping("/listUserGroupByUserId/{appId}/{userId}")
    @Operation(summary = "用户组列表查询", description = "查询某个用户所在的用户组列表")
    @PreAuthorize("hasAuthority('system:userGroup:query')")
    public Response<List<UserGroupLinkVO>> listUserGroupByUserId(@PathVariable String appId, @PathVariable String userId) {
        List<UserGroupLinkVO> tablePage = userGroupLinkService.listUserGroupByUserId(appId, userId);
        return HttpResult.ok(tablePage);
    }

    @GetMapping("/listUserGroupByRoleId/{roleId}")
    @Operation(summary = "用户组列表查询", description = "查询某个角色绑定的用户组列表")
    @PreAuthorize("hasAuthority('system:userGroup:query')")
    public Response<TablePage<UserGroupLinkVO>> listUserGroupByRoleId(@PathVariable String roleId, UserGroupLinkInfoDTO userGroupLinkInfoDTO, PageQuery pageQuery) {
        TablePage<UserGroupLinkVO> tablePage = userGroupRoleLinkService.listUserGroupByRoleId(roleId, userGroupLinkInfoDTO, pageQuery);
        return HttpResult.ok(tablePage);
    }

    @GetMapping("listWithDisabledByUserId/{appId}/{userId}")
    @Operation(summary = "用户组列表查询", description = "查询所有用户组列表，如果用户组存在用户，则 disabled 属性为 true")
    @PreAuthorize("hasAuthority('system:userGroup:query')")
    public Response<List<UserGroupBindSelectVO>> listWithDisabledByUserId(@PathVariable String appId, @PathVariable String userId) {
        List<UserGroupBindSelectVO> sysUserGroupVOList = userGroupLinkService.listWithDisabledByUserId(appId, userId);
        return HttpResult.ok(sysUserGroupVOList);
    }

    @GetMapping("listWithDisabledByRoleId/{roleId}")
    @Operation(summary = "用户组列表查询", description = "查询所有用户组列表，如果用户组绑定角色，则 disabled 属性为 true")
    @PreAuthorize("hasAuthority('system:userGroup:query')")
    public Response<List<UserGroupBindSelectVO>> listWithDisabledByRoleId(@PathVariable String roleId) {
        List<UserGroupBindSelectVO> sysUserGroupVOList = userGroupRoleLinkService.listWithDisabledByRoleId(roleId);
        return HttpResult.ok(sysUserGroupVOList);
    }

    @PostMapping("/addRolesToUserGroup")
    @Operation(summary = "添加角色到用户组", description = "添加角色到用户组（多个角色）")
    @OperateLog(title = "用户组角色关联管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('system:userGroup:add')")
    public Response<Boolean> addRolesToUserGroup(@Validated(RestGroup.AddGroup.class) @RequestBody UserGroupLinkRoleDTO userGroupLinkRoleDTO) {
        if (userGroupRoleLinkService.checkRolesExistUserGroup(userGroupLinkRoleDTO)) {
            return HttpResult.failMessage("添加角色到用户组失败，用户组已存在于角色中");
        }
        boolean result = userGroupRoleLinkService.addRolesToUserGroup(userGroupLinkRoleDTO);
        return HttpResult.ok(result);
    }

    @PostMapping("/addUsersToGroup")
    @Operation(summary = "添加用户到用户组", description = "添加用户到用户组（多个用户）")
    @OperateLog(title = "用户用户组关联管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('system:userGroup:add')")
    public Response<Boolean> addUsersToUserGroup(@Validated(RestGroup.AddGroup.class) @RequestBody UserGroupLinkUserDTO userGroupLinkUserDTO) {
        if (userGroupLinkService.checkUsersExistUserGroup(userGroupLinkUserDTO)) {
            return HttpResult.failMessage("添加用户到用户组失败，用户已存在于用户组中");
        }
        boolean result = userGroupLinkService.addUsersToUserGroup(userGroupLinkUserDTO);
        return HttpResult.ok(result);
    }

    @DeleteMapping("/removeUserFromUserGroup/{ids}")
    @Operation(summary = "移出用户组", description = "将用户移出项目组")
    @OperateLog(title = "用户用户组关联管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('system:userGroup:remove')")
    public Response<Boolean> removeUserFromUserGroup(@PathVariable Long[] ids) {
        boolean result = userGroupLinkService.removeUserFromUserGroup(List.of(ids));
        return HttpResult.ok(result);
    }

    @PutMapping("/editUserGroupUserLink")
    @Operation(summary = "用户关联用户信息修改", description = "修改用户组和用户䣌关联信息")
    @OperateLog(title = "用户用户组关联管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('system:userGroup:edit')")
    public Response<Boolean> editUserGroupUserLink(@Validated(RestGroup.EditGroup.class) @RequestBody UserGroupLinkDTO userGroupLinkDTO) {
        return HttpResult.ok(userGroupLinkService.updateOne(userGroupLinkDTO));
    }

    @PostMapping
    @Operation(summary = "用户组列表新增", description = "新增用户组")
    @OperateLog(title = "用户组管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('system:userGroup:add')")
    @PreventRepeatSubmit
    public Response<Boolean> addUserGroup(@Validated(RestGroup.AddGroup.class) @RequestBody SysUserGroupDTO sysUserGroupDTO) {
        if (sysUserGroupService.checkUserGroupNameUnique(sysUserGroupDTO)) {
            return HttpResult.failMessage("新增用户组「" + sysUserGroupDTO.getGroupName() + "」失败，用户组名称已存在");
        }

        return HttpResult.ok(sysUserGroupService.addUserGroup(sysUserGroupDTO));
    }

    @PutMapping
    @Operation(summary = "用户组列表修改", description = "修改用户组")
    @OperateLog(title = "用户组管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('system:userGroup:edit')")
    @PreventRepeatSubmit
    public Response<Boolean> editOne(@Validated(RestGroup.EditGroup.class) @RequestBody SysUserGroupDTO sysUserGroupDTO) {
        if (sysUserGroupService.checkUserGroupNameUnique(sysUserGroupDTO)) {
            return HttpResult.failMessage("修改用户组「" + sysUserGroupDTO.getGroupName() + "」失败，用户组名称已存在");
        }
        return HttpResult.ok(sysUserGroupService.editOne(sysUserGroupDTO));
    }

    @DeleteMapping("/{ids}")
    @Operation(summary = "用户组列表删除", description = "通过主键批量删除用户组列表")
    @OperateLog(title = "用户组管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('system:userGroup:remove')")
    @PreventRepeatSubmit
    public Response<Boolean> removeBatch(@PathVariable Long[] ids, @RequestBody List<String> userGroupIds) {
        return HttpResult.ok(sysUserGroupService.removeBatch(List.of(ids), userGroupIds));
    }

    @PostMapping("/export")
    @Operation(summary = "用户组数据导出", description = "导出用户组数据")
    @OperateLog(title = "用户组数据管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("hasAuthority('system:user:export')")
    public void export(@RequestBody SysUserGroupDTO sysUserGroupDTO, HttpServletResponse response) {
        List<SysUserGroupVO> sysUserGroupVOList = sysUserGroupService.listAll(sysUserGroupDTO);
        ExcelHelper.exportExcel(sysUserGroupVOList, "用户组数据", SysUserGroupVO.class, response);
    }
}
