package top.teek.uac.system.service;

import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.system.model.dto.UserRoleLinkDTO;
import top.teek.uac.system.model.dto.link.RoleLinkUserDTO;
import top.teek.uac.system.model.dto.link.UserLinkInfoDTO;
import top.teek.uac.system.model.dto.link.UserLinkRoleDTO;
import top.teek.uac.system.model.po.UserRoleLink;
import top.teek.uac.system.model.vo.link.RoleBindSelectVO;
import top.teek.uac.system.model.vo.link.RoleLinkVO;
import top.teek.uac.system.model.vo.link.UserBindSelectVO;
import top.teek.uac.system.model.vo.link.UserLinkVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_user_role_link(用户关联角色表)】的数据库操作Service
 */
public interface UserRoleLinkService extends IService<UserRoleLink> {

    boolean checkRoleExistUser(RoleLinkUserDTO roleLinkUserDTO);

    /**
     * 检查用户是否已存在某些角色中
     *
     * @return 是否存在
     */
    boolean checkUserExistRoles(UserLinkRoleDTO userLinkRoleDTO);

    /**
     * 添加用户到角色
     *
     * @param userLinkRoleDTO 用户绑定角色信息
     * @return 是否成功
     */
    boolean addRolesToUser(UserLinkRoleDTO userLinkRoleDTO);

    /**
     * 添加用户到角色
     *
     * @param roleLinkUserDTO 用户绑定角色信息
     * @return 是否成功
     */
    boolean addUsersToRole(RoleLinkUserDTO roleLinkUserDTO);

    /**
     * 更新记录
     *
     * @param userRoleLinkDTO 记录
     * @return 是否成功
     */
    boolean updateOne(UserRoleLinkDTO userRoleLinkDTO);

    /**
     * 将用户移出角色
     *
     * @param ids 关联 ID
     * @return 是否成功
     */
    boolean removeUserFromRole(List<Long> ids);

    /**
     * 通过角色 ID 查询用户列表
     *
     * @param roleId 角色 ID
     * @return 用户列表
     */
    TablePage<UserLinkVO> listUserLinkByRoleId(String roleId, UserLinkInfoDTO userLinkInfoDTO, PageQuery pageQuery);

    /**
     * 根据应用 ID、用户 ID 查询角色列表
     *
     * @param appId  应用ID
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RoleLinkVO> listRoleLinkByUserId(String appId, String userId);

    /**
     * 根据应用 ID、用户 ID 查询角色列表，如果角色绑定了用户，则 disabled 属性为 false
     *
     * @param appId  应用ID
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RoleBindSelectVO> listWithDisabledByUserId(String appId, String userId);

    /**
     * 下拉查询用户列表，如果用户绑定了角色，则 disabled 属性为 true
     *
     * @param roleId 角色 ID
     * @return 用户列表
     */
    List<UserBindSelectVO> listWithDisabledByRoleId(String roleId);

}
