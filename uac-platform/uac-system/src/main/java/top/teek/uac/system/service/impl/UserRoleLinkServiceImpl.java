package top.teek.uac.system.service.impl;

import top.teek.core.constants.ColumnConstant;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.system.mapper.UserRoleLinkMapper;
import top.teek.uac.system.model.dto.UserRoleLinkDTO;
import top.teek.uac.system.model.dto.link.RoleLinkUserDTO;
import top.teek.uac.system.model.dto.link.UserLinkInfoDTO;
import top.teek.uac.system.model.dto.link.UserLinkRoleDTO;
import top.teek.uac.system.model.po.SysRole;
import top.teek.uac.system.model.po.UserRoleLink;
import top.teek.uac.system.model.vo.link.RoleBindSelectVO;
import top.teek.uac.system.model.vo.link.RoleLinkVO;
import top.teek.uac.system.model.vo.link.UserBindSelectVO;
import top.teek.uac.system.model.vo.link.UserLinkVO;
import top.teek.uac.system.service.UserRoleLinkService;
import top.teek.utils.ListUtil;
import top.teek.utils.MapstructUtil;
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
 * @note 针对表【t_user_role_link(用户关联角色表)】的数据库操作Service实现
 */
@Service
public class UserRoleLinkServiceImpl extends ServiceImpl<UserRoleLinkMapper, UserRoleLink> implements UserRoleLinkService {

    @Override
    public boolean checkRoleExistUser(RoleLinkUserDTO roleLinkUserDTO) {
        return baseMapper.exists(Wrappers.<UserRoleLink>lambdaQuery()
                .eq(UserRoleLink::getRoleId, roleLinkUserDTO.getRoleId())
                .in(UserRoleLink::getUserId, roleLinkUserDTO.getUserIds())
                .eq(StringUtil.hasText(roleLinkUserDTO.getAppId()), UserRoleLink::getAppId, roleLinkUserDTO.getAppId()));
    }

    @Override
    public boolean checkUserExistRoles(UserLinkRoleDTO userLinkRoleDTO) {
        return baseMapper.exists(Wrappers.<UserRoleLink>lambdaQuery()
                .eq(UserRoleLink::getUserId, userLinkRoleDTO.getUserId())
                .in(UserRoleLink::getRoleId, userLinkRoleDTO.getRoleIds())
                .eq(StringUtil.hasText(userLinkRoleDTO.getAppId()), UserRoleLink::getAppId, userLinkRoleDTO.getAppId()));
    }

    @Override
    public boolean addRolesToUser(UserLinkRoleDTO userLinkRoleDTO) {
        List<String> roleIds = userLinkRoleDTO.getRoleIds();

        List<UserRoleLink> userRoleLinkList = ListUtil.newArrayList(roleIds, roleId ->
                        new UserRoleLink().setRoleId(roleId)
                                .setUserId(userLinkRoleDTO.getUserId())
                                .setValidFrom(userLinkRoleDTO.getValidFrom())
                                .setExpireOn(userLinkRoleDTO.getExpireOn())
                                .setAppId(userLinkRoleDTO.getAppId())
                , UserRoleLink.class);

        return Db.saveBatch(userRoleLinkList);
    }

    @Override
    public boolean addUsersToRole(RoleLinkUserDTO roleLinkUserDTO) {
        List<String> userIds = roleLinkUserDTO.getUserIds();

        List<UserRoleLink> userRoleLinkList = ListUtil.newArrayList(userIds, userId ->
                        new UserRoleLink().setUserId(userId)
                                .setRoleId(roleLinkUserDTO.getRoleId())
                                .setValidFrom(roleLinkUserDTO.getValidFrom())
                                .setExpireOn(roleLinkUserDTO.getExpireOn())
                                .setAppId(roleLinkUserDTO.getAppId())
                , UserRoleLink.class);

        return Db.saveBatch(userRoleLinkList);
    }

    @Override
    public boolean updateOne(UserRoleLinkDTO userRoleLinkDTO) {
        UserRoleLink userRoleLink = MapstructUtil.convert(userRoleLinkDTO, UserRoleLink.class);
        return baseMapper.updateById(userRoleLink) > 0;
    }

    @Override
    public boolean removeUserFromRole(List<Long> ids) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public TablePage<UserLinkVO> listUserLinkByRoleId(String roleId, UserLinkInfoDTO userLinkInfoDTO, PageQuery pageQuery) {
        QueryWrapper<UserRoleLink> queryWrapper = Wrappers.query();
        queryWrapper.eq("tsu.is_deleted", 0)
                .eq("turl.role_id", roleId)
                .like(StringUtil.hasText(userLinkInfoDTO.getUsername()), "tsu.username", userLinkInfoDTO.getUsername())
                .like(StringUtil.hasText(userLinkInfoDTO.getNickname()), "tsu.nickname", userLinkInfoDTO.getNickname());
        IPage<UserLinkVO> userLinkVOIPage = baseMapper.listUserLinkByRoleId(pageQuery.buildPage(), queryWrapper);

        return TablePage.build(userLinkVOIPage);
    }

    @Override
    public List<RoleLinkVO> listRoleLinkByUserId(String appId, String userId) {
        QueryWrapper<SysRole> wrapper = Wrappers.query();
        wrapper.eq("turl.is_deleted", ColumnConstant.NON_DELETED)
                .eq("tsr.app_id", appId)
                .eq("turl.user_id", userId);
        return baseMapper.listRoleLinkByUserId(wrapper);
    }

    @Override
    public List<RoleBindSelectVO> listWithDisabledByUserId(String appId, String userId) {
        return baseMapper.selectWithDisabledByUserId(appId, userId);
    }

    @Override
    public List<UserBindSelectVO> listWithDisabledByRoleId(String roleId) {
        return baseMapper.listWithDisabledByRoleId(roleId);
    }
}




