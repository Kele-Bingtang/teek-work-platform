package top.teek.uac.system.service;

import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.system.model.dto.UserGroupLinkDTO;
import top.teek.uac.system.model.dto.link.UserGroupLinkUserDTO;
import top.teek.uac.system.model.dto.link.UserLinkInfoDTO;
import top.teek.uac.system.model.dto.link.UserLinkUserGroupDTO;
import top.teek.uac.system.model.po.UserGroupLink;
import top.teek.uac.system.model.vo.link.UserBindSelectVO;
import top.teek.uac.system.model.vo.link.UserGroupBindSelectVO;
import top.teek.uac.system.model.vo.link.UserGroupLinkVO;
import top.teek.uac.system.model.vo.link.UserLinkVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_user_group_link(用户关联用户组表)】的数据库操作Service
 */
public interface UserGroupLinkService extends IService<UserGroupLink> {

    /**
     * 检查用户是否在某些用户组中（多个用户组）
     *
     * @return 是否在用户组中
     */
    boolean checkUserExistUserGroups(UserLinkUserGroupDTO userLinkUserGroupDTO);

    /**
     * 检查用户是否在某些用户组中（多个用户）
     *
     * @return 是否在用户组中
     */
    boolean checkUsersExistUserGroup(UserGroupLinkUserDTO userGroupLinkUserDTO);

    /**
     * 添加用户到用户组（多个用户组）
     *
     * @param userLinkUserGroupDTO 用户和用户组数据
     * @return 是否添加成功
     */
    boolean addUserGroupsToUser(UserLinkUserGroupDTO userLinkUserGroupDTO);

    /**
     * 添加用户到用户组（多个用户）
     *
     * @param userGroupLinkUserDTO 用户和用户组数据
     * @return 是否添加成功
     */
    boolean addUsersToUserGroup(UserGroupLinkUserDTO userGroupLinkUserDTO);

    /**
     * 将用户移出项目组
     *
     * @param ids 关联 ID
     * @return 是否移出成功
     */
    boolean removeUserFromUserGroup(List<Long> ids);

    /**
     * 通过用户 ID 查询用户组列表
     *
     * @param appId  应用 ID
     * @param userId 用户 ID
     * @return 用户组列表
     */
    List<UserGroupLinkVO> listUserGroupByUserId(String appId, String userId);

    /**
     * 查询用户组下的用户列表
     *
     * @param userGroupId 用户组 ID
     * @return 用户列表
     */
    TablePage<UserLinkVO> listUserLinkByGroupId(String userGroupId, UserLinkInfoDTO userLinkInfoDTO, PageQuery pageQuery);

    /**
     * 下拉查询用户列表，如果用户组绑定了用户，则 disabled 属性为 true
     *
     * @param appId  应用 ID
     * @param userId 用户 ID
     * @return 用户组列表
     */
    List<UserGroupBindSelectVO> listWithDisabledByUserId(String appId, String userId);

    /**
     * 下拉查询用户列表，如果用户绑定了用户组，则 disabled 属性为 true
     *
     * @param userGroupId 用户组 ID
     * @return 用户列表
     */
    List<UserBindSelectVO> listWithDisabledByGroupId(String userGroupId);

    /**
     * 修改用户组和用户的关联信息
     *
     * @param userGroupLinkDTO 用户组信息
     * @return 是否修改成功
     */
    boolean updateOne(UserGroupLinkDTO userGroupLinkDTO);

}
