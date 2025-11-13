package top.teek.uac.system.service.impl;

import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.system.mapper.UserGroupRoleLinkMapper;
import top.teek.uac.system.model.dto.link.RoleLinkInfoDTO;
import top.teek.uac.system.model.dto.link.RoleLinkUserGroupDTO;
import top.teek.uac.system.model.dto.link.UserGroupLinkInfoDTO;
import top.teek.uac.system.model.dto.link.UserGroupLinkRoleDTO;
import top.teek.uac.system.model.po.UserGroupLink;
import top.teek.uac.system.model.po.UserGroupRoleLink;
import top.teek.uac.system.service.UserGroupRoleLinkService;
import top.teek.uac.system.model.vo.link.RoleBindSelectVO;
import top.teek.uac.system.model.vo.link.RoleLinkVO;
import top.teek.uac.system.model.vo.link.UserGroupBindSelectVO;
import top.teek.uac.system.model.vo.link.UserGroupLinkVO;
import top.teek.utils.ListUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_user_group_role_link(用户组关联角色表)】的数据库操作Service实现
 */
@Service
public class UserGroupRoleLinkServiceImpl extends ServiceImpl<UserGroupRoleLinkMapper, UserGroupRoleLink> implements UserGroupRoleLinkService {

    @Override
    public boolean checkRolesExistUserGroup(UserGroupLinkRoleDTO userGroupLinkRoleDTO) {
        return baseMapper.exists(Wrappers.<UserGroupRoleLink>lambdaQuery()
                .in(UserGroupRoleLink::getRoleId, userGroupLinkRoleDTO.getRoleIds())
                .eq(UserGroupRoleLink::getUserGroupId, userGroupLinkRoleDTO.getUserGroupId())
                .eq(StringUtil.hasText(userGroupLinkRoleDTO.getAppId()), UserGroupRoleLink::getAppId, userGroupLinkRoleDTO.getAppId()));
    }

    @Override
    public boolean checkRoleExistUserGroups(RoleLinkUserGroupDTO roleLinkUserGroupDTO) {
        return baseMapper.exists(Wrappers.<UserGroupRoleLink>lambdaQuery()
                .eq(UserGroupRoleLink::getRoleId, roleLinkUserGroupDTO.getRoleId())
                .in(UserGroupRoleLink::getUserGroupId, roleLinkUserGroupDTO.getUserGroupIds())
                .eq(StringUtil.hasText(roleLinkUserGroupDTO.getAppId()), UserGroupRoleLink::getAppId, roleLinkUserGroupDTO.getAppId()));
    }

    @Override
    public boolean addRolesToUserGroup(UserGroupLinkRoleDTO userGroupLinkRoleDTO) {
        List<String> roleIds = userGroupLinkRoleDTO.getRoleIds();

        List<UserGroupRoleLink> userGroupLinkList = ListUtil.newArrayList(roleIds, roleId ->
                        new UserGroupRoleLink().setRoleId(roleId)
                                .setUserGroupId(userGroupLinkRoleDTO.getUserGroupId())
                                .setAppId(userGroupLinkRoleDTO.getAppId())
                , UserGroupRoleLink.class);

        return Db.saveBatch(userGroupLinkList);
    }

    @Override
    public boolean addUserGroupsToRole(RoleLinkUserGroupDTO roleLinkUserGroupDTO) {
        List<String> userGroupIds = roleLinkUserGroupDTO.getUserGroupIds();

        List<UserGroupRoleLink> userGroupLinkList = ListUtil.newArrayList(userGroupIds, userGroupId ->
                        new UserGroupRoleLink().setUserGroupId(userGroupId)
                                .setRoleId(roleLinkUserGroupDTO.getRoleId())
                                .setAppId(roleLinkUserGroupDTO.getAppId())
                , UserGroupRoleLink.class);

        return Db.saveBatch(userGroupLinkList);
    }

    @Override
    public boolean removeUserGroupFromRole(List<Long> ids) {
        return baseMapper.deleteByIds(ids) > 0;
    }

    @Override
    public TablePage<RoleLinkVO> listRoleLinkByGroupId(String userGroupId, RoleLinkInfoDTO roleLinkInfoDTO, PageQuery pageQuery) {
        QueryWrapper<UserGroupLink> queryWrapper = Wrappers.query();
        queryWrapper.eq("tsr.is_deleted", 0)
                .eq("tugrl.user_group_id", userGroupId)
                .like(StringUtil.hasText(roleLinkInfoDTO.getRoleCode()), "tsr.role_code", roleLinkInfoDTO.getRoleCode())
                .like(StringUtil.hasText(roleLinkInfoDTO.getRoleName()), "tsr.role_name", roleLinkInfoDTO.getRoleName());

        IPage<RoleLinkVO> userGroupLinkRoleVOIPage = baseMapper.listRoleLinkByGroupId(pageQuery.buildPage(), queryWrapper);

        return TablePage.build(userGroupLinkRoleVOIPage);
    }

    @Override
    public TablePage<UserGroupLinkVO> listUserGroupByRoleId(String roleId, UserGroupLinkInfoDTO userGroupLinkInfoDTO, PageQuery pageQuery) {
        QueryWrapper<UserGroupLink> queryWrapper = Wrappers.query();
        queryWrapper.eq("tsug.is_deleted", 0)
                .eq("tugrl.role_id", roleId)
                .like(StringUtil.hasText(userGroupLinkInfoDTO.getUserGroupName()), "tsug.group_name", userGroupLinkInfoDTO.getUserGroupName())
                .like(StringUtil.hasText(userGroupLinkInfoDTO.getOwner()), "concat(tsug.owner_id, ',', tsug.owner_name)", userGroupLinkInfoDTO.getOwner());
        IPage<UserGroupLinkVO> userGroupLinkVOIPage = baseMapper.listUserGroupByRoleId(pageQuery.buildPage(), queryWrapper);

        return TablePage.build(userGroupLinkVOIPage);
    }

    @Override
    public List<RoleBindSelectVO> listWithDisabledByGroupId(String userGroupId) {
        return baseMapper.listWithDisabledByGroupId(userGroupId);
    }

    @Override
    public List<UserGroupBindSelectVO> listWithDisabledByRoleId(String roleId) {
        return baseMapper.listWithDisabledByRoleId(roleId);
    }

}




