package top.teek.uac.system.service.impl;

import top.teek.core.constants.ColumnConstant;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.system.mapper.UserGroupLinkMapper;
import top.teek.uac.system.model.dto.UserGroupLinkDTO;
import top.teek.uac.system.model.dto.link.UserGroupLinkUserDTO;
import top.teek.uac.system.model.dto.link.UserLinkInfoDTO;
import top.teek.uac.system.model.dto.link.UserLinkUserGroupDTO;
import top.teek.uac.system.model.po.SysUserGroup;
import top.teek.uac.system.model.po.UserGroupLink;
import top.teek.uac.system.model.vo.link.UserBindSelectVO;
import top.teek.uac.system.model.vo.link.UserGroupBindSelectVO;
import top.teek.uac.system.model.vo.link.UserGroupLinkVO;
import top.teek.uac.system.model.vo.link.UserLinkVO;
import top.teek.uac.system.service.UserGroupLinkService;
import top.teek.utils.ListUtil;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_user_group_link(用户关联用户组表)】的数据库操作Service实现
 */
@Service
@RequiredArgsConstructor
public class UserGroupLinkServiceImpl extends ServiceImpl<UserGroupLinkMapper, UserGroupLink> implements UserGroupLinkService {

    @Override
    public boolean checkUserExistUserGroups(UserLinkUserGroupDTO userLinkUserGroupDTO) {
        return baseMapper.exists(Wrappers.<UserGroupLink>lambdaQuery()
                .eq(UserGroupLink::getUserId, userLinkUserGroupDTO.getUserId())
                .in(UserGroupLink::getUserGroupId, userLinkUserGroupDTO.getUserGroupIds()));
    }

    @Override
    public boolean checkUsersExistUserGroup(UserGroupLinkUserDTO userGroupLinkUserDTO) {
        return baseMapper.exists(Wrappers.<UserGroupLink>lambdaQuery()
                .in(UserGroupLink::getUserId, userGroupLinkUserDTO.getUserIds())
                .eq(UserGroupLink::getUserGroupId, userGroupLinkUserDTO.getUserGroupId())
        );
    }

    @Override
    public boolean addUserGroupsToUser(UserLinkUserGroupDTO userLinkUserGroupDTO) {
        List<String> userGroupIds = userLinkUserGroupDTO.getUserGroupIds();

        List<UserGroupLink> userGroupLinkList = ListUtil.newArrayList(userGroupIds, userGroupId ->
                        new UserGroupLink().setUserGroupId(userGroupId)
                                .setUserId(userLinkUserGroupDTO.getUserId())
                                .setValidFrom(userLinkUserGroupDTO.getValidFrom())
                                .setExpireOn(userLinkUserGroupDTO.getExpireOn())
                , UserGroupLink.class);

        return Db.saveBatch(userGroupLinkList);
    }

    @Override
    public boolean addUsersToUserGroup(UserGroupLinkUserDTO userGroupLinkUserDTO) {
        List<String> userIds = userGroupLinkUserDTO.getUserIds();

        List<UserGroupLink> userGroupLinkList = ListUtil.newArrayList(userIds, userId ->
                        new UserGroupLink().setUserId(userId)
                                .setUserGroupId(userGroupLinkUserDTO.getUserGroupId())
                                .setValidFrom(userGroupLinkUserDTO.getValidFrom())
                                .setExpireOn(userGroupLinkUserDTO.getExpireOn())
                , UserGroupLink.class);

        return Db.saveBatch(userGroupLinkList);
    }

    @Override
    public boolean removeUserFromUserGroup(List<Long> ids) {
        return baseMapper.deleteByIds(ids) > 0;
    }

    @Override
    public List<UserGroupLinkVO> listUserGroupByUserId(String userId) {
        QueryWrapper<SysUserGroup> wrapper = Wrappers.query();
        wrapper.eq("tugl.is_deleted", ColumnConstant.NON_DELETED)
                .eq("tugl.user_id", userId);

        return baseMapper.listUserGroupByUserId(wrapper);
    }

    @Override
    public TablePage<UserLinkVO> listUserLinkByGroupId(String userGroupId, UserLinkInfoDTO userLinkInfoDTO, PageQuery pageQuery) {
        QueryWrapper<UserGroupLink> queryWrapper = Wrappers.query();
        queryWrapper.eq("tsu.is_deleted", 0)
                .eq("tugl.user_group_id", userGroupId)
                .like(StringUtil.hasText(userLinkInfoDTO.getUsername()), "tsu.username", userLinkInfoDTO.getUsername())
                .like(StringUtil.hasText(userLinkInfoDTO.getNickname()), "tsu.nickname", userLinkInfoDTO.getNickname());
        IPage<UserLinkVO> userLinkVOIPage = baseMapper.listUserLinkByGroupId(pageQuery.buildPage(), queryWrapper);

        return TablePage.build(userLinkVOIPage);
    }

    @Override
    public List<UserGroupBindSelectVO> listWithDisabledByUserId(String appId, String userId) {
        return baseMapper.selectWithDisabledByUserId(appId, userId);
    }

    @Override
    public List<UserBindSelectVO> listWithDisabledByGroupId(String userGroupId) {
        return baseMapper.listWithDisabledByGroupId(userGroupId);
    }


    @Override
    public boolean updateOne(UserGroupLinkDTO userGroupLinkDTO) {
        UserGroupLink userGroupLink = MapstructUtil.convert(userGroupLinkDTO, UserGroupLink.class);
        return baseMapper.updateById(userGroupLink) > 0;
    }
}




