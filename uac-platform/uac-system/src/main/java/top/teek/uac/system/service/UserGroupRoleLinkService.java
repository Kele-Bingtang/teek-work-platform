package top.teek.uac.system.service;

import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.system.model.dto.link.RoleLinkInfoDTO;
import top.teek.uac.system.model.dto.link.RoleLinkUserGroupDTO;
import top.teek.uac.system.model.dto.link.UserGroupLinkInfoDTO;
import top.teek.uac.system.model.dto.link.UserGroupLinkRoleDTO;
import top.teek.uac.system.model.po.UserGroupRoleLink;
import top.teek.uac.system.model.vo.link.RoleBindSelectVO;
import top.teek.uac.system.model.vo.link.RoleLinkVO;
import top.teek.uac.system.model.vo.link.UserGroupBindSelectVO;
import top.teek.uac.system.model.vo.link.UserGroupLinkVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_user_group_role_link(用户组关联角色表)】的数据库操作Service
 */
public interface UserGroupRoleLinkService extends IService<UserGroupRoleLink> {
    /**
     * 检查角色是否已存在用户组
     *
     * @return 是否存在
     */
    boolean checkRolesExistUserGroup(UserGroupLinkRoleDTO userGroupLinkRoleDTO);


    /**
     * 检查用户组是否已存在角色
     *
     * @return 是否存在
     */
    boolean checkRoleExistUserGroups(RoleLinkUserGroupDTO roleLinkUserGroupDTO);

    /**
     * 添加角色到用户组（多个角色）
     *
     * @param userGroupLinkRoleDTO 用户组角色关联实体
     * @return 是否成功
     */
    boolean addRolesToUserGroup(UserGroupLinkRoleDTO userGroupLinkRoleDTO);

    /**
     * 添加用户到用户组（多个用户组）
     *
     * @param roleLinkUserGroupDTO 角色用户组关联实体
     * @return 是否成功
     */
    boolean addUserGroupsToRole(RoleLinkUserGroupDTO roleLinkUserGroupDTO);

    /**
     * 将用户组移出角色
     *
     * @param ids 用户组 id 集合
     * @return 是否成功
     */
    boolean removeUserGroupFromRole(List<Long> ids);

    /**
     * 通过用户组 ID 查询角色列表（分页）
     *
     * @param userGroupId 用户组 ID
     * @return 角色列表
     */
    TablePage<RoleLinkVO> listRoleLinkByGroupId(String userGroupId, RoleLinkInfoDTO roleLinkInfoDTO, PageQuery pageQuery);

    /**
     * 查询某个角色绑定的用户组列表
     * @param roleId 角色 ID
     * @return 用户组列表
     */
    TablePage<UserGroupLinkVO> listUserGroupByRoleId(String roleId, UserGroupLinkInfoDTO userGroupLinkInfoDTO, PageQuery pageQuery);

    /**
     * 查询角色列表（已选的被禁用）
     *
     * @param userGroupId 用户组 ID
     * @return 角色列表
     */
    List<RoleBindSelectVO> listWithDisabledByGroupId(String userGroupId);
    
    /**
     * 查询所有用户组列表，如果用户组绑定角色，则 disabled 属性为 true
     *
     * @param roleId 角色 ID
     * @return 用户组列表
     */
    List<UserGroupBindSelectVO> listWithDisabledByRoleId(String roleId);

}
