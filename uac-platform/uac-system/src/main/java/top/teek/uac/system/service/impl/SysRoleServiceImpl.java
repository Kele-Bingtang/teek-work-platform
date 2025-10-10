package top.teek.uac.system.service.impl;

import top.teek.core.exception.ServiceException;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.core.constant.TenantConstant;
import top.teek.uac.system.mapper.SysRoleMapper;
import top.teek.uac.system.model.dto.SysRoleDTO;
import top.teek.uac.system.model.po.*;
import top.teek.uac.system.model.vo.SysRoleVO;
import top.teek.uac.system.service.*;
import top.teek.uac.system.model.po.*;
import top.teek.utils.ListUtil;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.teek.uac.system.service.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_sys_role(应用角色信息表)】的数据库操作Service实现
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final RoleMenuLinkService roleMenuLinkService;
    private final RoleDeptLinkService roleDeptLinkService;
    private final UserRoleLinkService userRoleLinkService;
    private final UserGroupRoleLinkService userGroupRoleLinkService;

    @Override
    public List<SysRoleVO> listAll(SysRoleDTO sysRoleDTO) {
        LambdaQueryWrapper<SysRole> wrapper = buildQueryWrapper(sysRoleDTO);
        List<SysRole> sysRoleList = baseMapper.selectList(wrapper);

        return MapstructUtil.convert(sysRoleList, SysRoleVO.class);
    }

    @Override
    public TablePage<SysRoleVO> listPage(SysRoleDTO sysRoleDTO, PageQuery pageQuery) {
        LambdaQueryWrapper<SysRole> wrapper = buildQueryWrapper(sysRoleDTO);
        Page<SysRole> sysRolePage = baseMapper.selectPage(pageQuery.buildPage(), wrapper);

        return TablePage.build(sysRolePage, SysRoleVO.class);
    }

    private LambdaQueryWrapper<SysRole> buildQueryWrapper(SysRoleDTO sysRoleDTO) {
        return Wrappers.<SysRole>lambdaQuery()
                .eq(StringUtil.hasText(sysRoleDTO.getRoleId()), SysRole::getRoleId, sysRoleDTO.getRoleId())
                .eq(StringUtil.hasText(sysRoleDTO.getRoleCode()), SysRole::getRoleCode, sysRoleDTO.getRoleCode())
                .eq(StringUtil.hasText(sysRoleDTO.getAppId()), SysRole::getAppId, sysRoleDTO.getAppId())
                .eq(Objects.nonNull(sysRoleDTO.getStatus()), SysRole::getStatus, sysRoleDTO.getStatus())
                .orderByAsc(SysRole::getOrderNum);

    }

    @Override
    public boolean checkRoleCodeUnique(SysRoleDTO sysRoleDTO) {
        return baseMapper.exists(Wrappers.<SysRole>lambdaQuery()
                .eq(SysRole::getRoleCode, sysRoleDTO.getRoleCode())
                .ne(Objects.nonNull(sysRoleDTO.getId()), SysRole::getId, sysRoleDTO.getId()));
    }

    @Override
    public boolean checkRoleNameUnique(SysRoleDTO sysRoleDTO) {
        return baseMapper.exists(Wrappers.<SysRole>lambdaQuery()
                .eq(SysRole::getRoleName, sysRoleDTO.getRoleName())
                .ne(Objects.nonNull(sysRoleDTO.getId()), SysRole::getId, sysRoleDTO.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addRole(SysRoleDTO sysRoleDTO) {
        checkRoleAllowed(sysRoleDTO);
        SysRole sysRole = MapstructUtil.convert(sysRoleDTO, SysRole.class);
        int result = baseMapper.insert(sysRole);
        if (ListUtil.isNotEmpty(sysRoleDTO.getSelectedMenuIds())) {
            sysRoleDTO.setRoleId(sysRole.getRoleId());
            return roleMenuLinkService.addMenusToRole(sysRoleDTO, false);
        }
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editRole(SysRoleDTO sysRoleDTO) {
        checkRoleAllowed(sysRoleDTO);
        SysRole sysRole = MapstructUtil.convert(sysRoleDTO, SysRole.class);
        int result = baseMapper.updateById(sysRole);
        if (ListUtil.isNotEmpty(sysRoleDTO.getSelectedMenuIds())) {
            return roleMenuLinkService.addMenusToRole(sysRoleDTO, true);
        }
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeBatch(List<Long> ids, List<String> roleIds) {
        // 校验是否删除的是管理员角色
        roleIds.forEach(roleId -> {
            SysRoleDTO sysRoleDTO = new SysRoleDTO();
            sysRoleDTO.setRoleId(roleId);
            checkRoleAllowed(sysRoleDTO);
        });

        // 删除角色与菜单关联
        roleMenuLinkService.remove(Wrappers.<RoleMenuLink>lambdaQuery().in(RoleMenuLink::getRoleId, roleIds));
        // 删除角色与部门关联
        roleDeptLinkService.remove(Wrappers.<RoleDeptLink>lambdaQuery().in(RoleDeptLink::getRoleId, roleIds));
        // 删除角色与用户绑定
        userRoleLinkService.remove(Wrappers.<UserRoleLink>lambdaQuery().in(UserRoleLink::getRoleId, roleIds));
        // 删除角色与用户组绑定
        userGroupRoleLinkService.remove(Wrappers.<UserGroupRoleLink>lambdaQuery().in(UserGroupRoleLink::getRoleId, roleIds));
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public boolean checkAppExitRole(List<String> appIds) {
        return baseMapper.exists(Wrappers.<SysRole>lambdaQuery()
                .in(SysRole::getAppId, appIds));
    }

    @Override
    public void checkRoleAllowed(SysRoleDTO sysRoleDTO) {
        if (Objects.nonNull(sysRoleDTO.getRoleId()) && TenantConstant.DEFAULT_ROLE_ID.equals(sysRoleDTO.getRoleId())) {
            throw new ServiceException("不允许操作超级管理员角色");
        }

        String[] keys = new String[]{TenantConstant.SUPER_ADMIN_ROLE_KEY, TenantConstant.TENANT_ADMIN_ROLE_KEY};

        // 新增不允许使用 管理员标识符
        if (Objects.isNull(sysRoleDTO.getRoleId())
                && StringUtils.equalsAny(sysRoleDTO.getRoleCode(), keys)) {
            throw new ServiceException("不允许使用系统内置管理员角色标识符!");
        }
        // 修改不允许修改 管理员标识符
        if (Objects.nonNull(sysRoleDTO.getRoleId())) {
            SysRole sysRole = baseMapper.selectOne(Wrappers.<SysRole>lambdaQuery()
                    .eq(SysRole::getRoleId, sysRoleDTO.getRoleId())
                    .eq(Objects.nonNull(sysRoleDTO.getAppId()), SysRole::getAppId, sysRoleDTO.getAppId())
            );
            // 如果标识符不相等 判断为修改了管理员标识符
            if (!StringUtils.equals(sysRole.getRoleCode(), sysRoleDTO.getRoleCode())
                    && StringUtils.equalsAny(sysRole.getRoleCode(), keys)) {
                throw new ServiceException("不允许修改系统内置管理员角色标识符!");
            }
        }
    }

    @Override
    public Set<String> listRoleCodesByUserId(String userId) {
        List<SysRole> sysRoleList = baseMapper.selectRoleListByUserId(userId);
        return sysRoleList.stream().map(SysRole::getRoleCode).collect(Collectors.toSet());
    }
}




