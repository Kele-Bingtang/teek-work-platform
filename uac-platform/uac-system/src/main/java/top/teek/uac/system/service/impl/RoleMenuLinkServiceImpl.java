package top.teek.uac.system.service.impl;

import top.teek.uac.system.mapper.RoleMenuLinkMapper;
import top.teek.uac.system.model.dto.SysRoleDTO;
import top.teek.uac.system.model.po.RoleMenuLink;
import top.teek.uac.system.model.po.SysMenu;
import top.teek.uac.system.service.RoleMenuLinkService;
import top.teek.utils.ListUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_role_menu_link(角色关联菜单表)】的数据库操作Service实现
 */
@Service
public class RoleMenuLinkServiceImpl extends ServiceImpl<RoleMenuLinkMapper, RoleMenuLink> implements RoleMenuLinkService {


    @Override
    public List<String> listMenuIdsByRoleId(String roleId, String appId, String tenantId) {

        List<RoleMenuLink> roleMenuLinks = baseMapper.selectList(Wrappers.<RoleMenuLink>lambdaQuery()
                .eq(RoleMenuLink::getRoleId, roleId)
                .eq(StringUtil.hasText(appId), RoleMenuLink::getAppId, appId)
                .eq(StringUtil.hasText(tenantId), RoleMenuLink::getTenantId, tenantId)
        );

        if (ListUtil.isNotEmpty(roleMenuLinks)) {
            return roleMenuLinks.stream().map(RoleMenuLink::getMenuId).toList();
        }

        return List.of();
    }

    @Override
    public List<SysMenu> listMenuListByRoleId(String roleId, String appId) {
        return baseMapper.listMenuListByRoleId(roleId, appId);
    }

    @Override
    public boolean addMenusToRole(SysRoleDTO sysRoleDTO, boolean removeLink) {
        if (removeLink) {
            // 删除角色与菜单关联
            baseMapper.delete(Wrappers.<RoleMenuLink>lambdaQuery()
                    .eq(RoleMenuLink::getRoleId, sysRoleDTO.getRoleId()));
        }

        List<String> selectedMenuIds = sysRoleDTO.getSelectedMenuIds();

        List<RoleMenuLink> roleMenuLinkList = ListUtil.newArrayList(selectedMenuIds, menuId ->
                        new RoleMenuLink().setMenuId(menuId)
                                .setRoleId(sysRoleDTO.getRoleId())
                                .setAppId(sysRoleDTO.getAppId())
                , RoleMenuLink.class);

        return Db.saveBatch(roleMenuLinkList);
    }
}




