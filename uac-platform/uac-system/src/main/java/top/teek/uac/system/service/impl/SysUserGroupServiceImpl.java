package top.teek.uac.system.service.impl;

import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.system.mapper.SysUserGroupMapper;
import top.teek.uac.system.model.dto.SysUserGroupDTO;
import top.teek.uac.system.model.po.SysUserGroup;
import top.teek.uac.system.model.po.UserGroupLink;
import top.teek.uac.system.model.po.UserGroupRoleLink;
import top.teek.uac.system.model.vo.SysUserGroupVO;
import top.teek.uac.system.service.SysUserGroupService;
import top.teek.uac.system.service.UserGroupLinkService;
import top.teek.uac.system.service.UserGroupRoleLinkService;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_sys_user_group(用户组表)】的数据库操作Service实现
 */
@Service
@RequiredArgsConstructor
public class SysUserGroupServiceImpl extends ServiceImpl<SysUserGroupMapper, SysUserGroup> implements SysUserGroupService {

    private final UserGroupLinkService userGroupLinkService;
    private final UserGroupRoleLinkService userGroupRoleLinkService;
    
    @Override
    public List<SysUserGroupVO> listAll(SysUserGroupDTO sysUserGroupDTO) {
        LambdaQueryWrapper<SysUserGroup> wrapper = buildQueryWrapper(sysUserGroupDTO);
        List<SysUserGroup> sysUserGroupList = baseMapper.selectList(wrapper);

        return MapstructUtil.convert(sysUserGroupList, SysUserGroupVO.class);
    }

    @Override
    public TablePage<SysUserGroupVO> listPage(SysUserGroupDTO sysUserGroupDTO, PageQuery pageQuery) {
        LambdaQueryWrapper<SysUserGroup> wrapper = buildQueryWrapper(sysUserGroupDTO);
        Page<SysUserGroup> sysUserGroupPage = baseMapper.selectPage(pageQuery.buildPage(), wrapper);
        
        return TablePage.build(sysUserGroupPage, SysUserGroupVO.class);
    }

    private LambdaQueryWrapper<SysUserGroup> buildQueryWrapper(SysUserGroupDTO sysUserGroupDTO) {
        return Wrappers.<SysUserGroup>lambdaQuery()
                .eq(StringUtil.hasText(sysUserGroupDTO.getGroupName()), SysUserGroup::getGroupName, sysUserGroupDTO.getGroupName())
                .eq(StringUtil.hasText(sysUserGroupDTO.getAppId()), SysUserGroup::getAppId, sysUserGroupDTO.getAppId())
                .orderByAsc(SysUserGroup::getCreateTime);
    }

    @Override
    public boolean checkUserGroupNameUnique(SysUserGroupDTO sysUserGroupDTO) {
        return baseMapper.exists(Wrappers.<SysUserGroup>lambdaQuery()
                .eq(SysUserGroup::getGroupName, sysUserGroupDTO.getGroupName())
                .ne(Objects.nonNull(sysUserGroupDTO.getId()), SysUserGroup::getId, sysUserGroupDTO.getId()));
    }

    @Override
    public boolean addUserGroup(SysUserGroupDTO sysUserGroupDTO) {
        SysUserGroup userGroup = MapstructUtil.convert(sysUserGroupDTO, SysUserGroup.class);
        return baseMapper.insert(userGroup) > 0;
    }

    @Override
    public boolean editOne(SysUserGroupDTO sysUserGroupDTO) {
        SysUserGroup userGroup = MapstructUtil.convert(sysUserGroupDTO, SysUserGroup.class);
        return baseMapper.updateById(userGroup) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeBatch(List<Long> ids, List<String> userGroupIds) {
        // 删除用户组与用户绑定
        userGroupLinkService.remove(Wrappers.<UserGroupLink>lambdaQuery().in(UserGroupLink::getUserGroupId, userGroupIds));
        // 删除用户组与角色绑定
        userGroupRoleLinkService.remove(Wrappers.<UserGroupRoleLink>lambdaQuery().in(UserGroupRoleLink::getUserGroupId, userGroupIds));
        return baseMapper.deleteBatchIds(ids) > 0;
    }

}




