package top.teek.uac.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import top.teek.core.constants.ColumnConstant;
import top.teek.core.exception.ServiceException;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.security.domain.LoginUser;
import top.teek.uac.core.constant.TenantConstant;
import top.teek.uac.core.enums.MenuType;
import top.teek.uac.core.helper.UacHelper;
import top.teek.uac.system.mapper.SysMenuMapper;
import top.teek.uac.system.model.dto.SysMenuDTO;
import top.teek.uac.system.model.po.RoleMenuLink;
import top.teek.uac.system.model.po.SysMenu;
import top.teek.uac.system.model.vo.SysMenuVO;
import top.teek.uac.system.model.vo.router.Meta;
import top.teek.uac.system.model.vo.router.RouterVO;
import top.teek.uac.system.service.RoleMenuLinkService;
import top.teek.uac.system.service.SysMenuService;
import top.teek.utils.ListUtil;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import top.teek.utils.TreeBuildUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_sys_menu(菜单表)】的数据库操作Service实现
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final RoleMenuLinkService roleMenuLinkService;

    @Override
    public List<RouterVO> listRoutes(String appId) {
        // 查询用户拥有的菜单列表
        LoginUser loginUser = UacHelper.getLoginUser();
        if (Objects.isNull(loginUser)) {
            throw new ServiceException("查询不到登录用户的信息");
        }

        List<SysMenu> sysMenuList = baseMapper.listMenuListByUserId(appId, loginUser.getUserId(), false);
        List<SysMenuVO> sysMenuVOList = MapstructUtil.convert(sysMenuList, SysMenuVO.class);

        List<SysMenuVO> treeList = TreeBuildUtil.build(sysMenuVOList, "0", SysMenuVO::getMenuId);

        // 构建前端需要的路由列表
        return buildRoutes(treeList);

    }

    private List<RouterVO> buildRoutes(List<SysMenuVO> treeList) {
        List<RouterVO> routers = new ArrayList<>();
        for (SysMenuVO sysMenuVO : treeList) {
            // 按钮权限过滤掉
            if (MenuType.FUNCTION.getValue().equals(sysMenuVO.getMenuType())) {
                continue;
            }
            RouterVO router = new RouterVO()
                    .setPath(sysMenuVO.getPath())
                    .setName(sysMenuVO.getMenuCode())
                    .setComponent(sysMenuVO.getComponent())
                    .setMeta(sysMenuVO.getMeta());

            List<SysMenuVO> childMenus = sysMenuVO.getChildren();
            if (ListUtil.isNotEmpty(childMenus)) {
                // 每个路由菜单添加 Auths 按钮权限
                childMenus.forEach(childMenu -> {
                    if (MenuType.FUNCTION.getValue().equals(childMenu.getMenuType())) {
                        Meta meta = router.getMeta();
                        Set<String> auths = Optional.ofNullable(meta.getAuths()).orElse(new HashSet<>());
                        // 按钮权限可能是多个，所以需要拆分
                        String[] split = Optional.ofNullable(childMenu.getPermission()).orElse("").split(",");
                        auths.addAll(Arrays.asList(split));
                        meta.setAuths(auths);
                        router.setMeta(meta);
                    }
                });
                router.setChildren(buildRoutes(childMenus));
            }
            routers.add(router);
        }
        return routers;
    }

    @Override
    public List<SysMenuVO> listAll(SysMenuDTO sysMenuDTO) {
        LambdaQueryWrapper<SysMenu> wrapper = buildQueryWrapper(sysMenuDTO);
        List<SysMenu> sysMenuList = baseMapper.selectList(wrapper);

        return MapstructUtil.convert(sysMenuList, SysMenuVO.class);
    }

    @Override
    public TablePage<SysMenuVO> listPage(SysMenuDTO sysMenuDTO, PageQuery pageQuery) {
        LambdaQueryWrapper<SysMenu> wrapper = buildQueryWrapper(sysMenuDTO);
        Page<SysMenu> sysMenuPage = baseMapper.selectPage(pageQuery.buildPage(), wrapper);

        return TablePage.build(sysMenuPage, SysMenuVO.class);
    }

    /**
     * 构建前端需要的菜单树形结构
     */
    @Override
    public List<Tree<String>> listMenuTreeSelect(SysMenuDTO sysMenuDTO) {
        // 查询正常状态的部门
        sysMenuDTO.setStatus(ColumnConstant.STATUS_NORMAL);
        LambdaQueryWrapper<SysMenu> wrapper = buildQueryWrapper(sysMenuDTO);
        List<SysMenu> sysMenuList = baseMapper.selectList(wrapper);
        return buildMenuTree(sysMenuList);
    }

    @Override
    public List<SysMenuVO> listMenuTreeTable(SysMenuDTO sysMenuDTO) {
        LambdaQueryWrapper<SysMenu> wrapper = buildQueryWrapper(sysMenuDTO);
        List<SysMenu> sysMenuList = baseMapper.selectList(wrapper);
        List<SysMenuVO> menuTreeList = MapstructUtil.convert(sysMenuList, SysMenuVO.class);

        return TreeBuildUtil.build(menuTreeList, "0", SysMenuVO::getMenuId);
    }

    private LambdaQueryWrapper<SysMenu> buildQueryWrapper(SysMenuDTO sysMenuDTO) {
        return Wrappers.<SysMenu>lambdaQuery()
                .eq(StringUtil.hasText(sysMenuDTO.getMenuCode()), SysMenu::getMenuCode, sysMenuDTO.getMenuCode())
                .eq(StringUtil.hasText(sysMenuDTO.getMenuName()), SysMenu::getMenuName, sysMenuDTO.getMenuName())
                .eq(StringUtil.hasText(sysMenuDTO.getAppId()), SysMenu::getAppId, sysMenuDTO.getAppId())
                .eq(Objects.nonNull(sysMenuDTO.getStatus()), SysMenu::getStatus, sysMenuDTO.getStatus())
                .orderByAsc(SysMenu::getParentId)
                .orderByAsc(SysMenu::getOrderNum);
    }

    @Override
    public List<SysMenuVO> listMenuListByUserId(String appId, String userId) {
        List<SysMenu> sysMenuList = baseMapper.listMenuListByUserId(appId, userId, false);
        return MapstructUtil.convert(sysMenuList, SysMenuVO.class);
    }

    @Override
    public List<Tree<String>> listMenuListByRoleId(String roleId, String appId) {
        List<SysMenu> sysMenuList = roleMenuLinkService.listMenuListByRoleId(roleId, appId);
        return buildMenuTree(sysMenuList);
    }

    /**
     * 构建前端所需要下拉树结构
     */
    private List<Tree<String>> buildMenuTree(List<SysMenu> sysMenuList) {
        if (CollUtil.isEmpty(sysMenuList)) {
            return Collections.emptyList();
        }

        return TreeBuildUtil.build(sysMenuList, ColumnConstant.PARENT_ID, TreeNodeConfig.DEFAULT_CONFIG.setIdKey("value").setNameKey("label"), (treeNode, tree) -> {
                    tree.setId(treeNode.getMenuId())
                            .setParentId(treeNode.getParentId())
                            .setName(treeNode.getMenuName())
                            .setWeight(treeNode.getOrderNum())
                            .putExtra("icon", treeNode.getIcon());

                    // 如果节点是选中状态，则设置选中样式
                    tree.putExtra("class", treeNode.isSelected() ? "selected" : "");
                }
        );
    }

    @Override
    public boolean checkMenuCodeUnique(SysMenuDTO sysMenuDTO) {
        return baseMapper.exists(Wrappers.<SysMenu>lambdaQuery()
                .eq(SysMenu::getMenuCode, sysMenuDTO.getMenuCode())
                .eq(SysMenu::getParentId, sysMenuDTO.getParentId())
                .ne(Objects.nonNull(sysMenuDTO.getId()), SysMenu::getId, sysMenuDTO.getId()));
    }

    @Override
    public boolean checkMenuNameUnique(SysMenuDTO sysMenuDTO) {
        return baseMapper.exists(Wrappers.<SysMenu>lambdaQuery()
                .eq(SysMenu::getMenuName, sysMenuDTO.getMenuName())
                .eq(SysMenu::getParentId, sysMenuDTO.getParentId())
                .ne(Objects.nonNull(sysMenuDTO.getId()), SysMenu::getId, sysMenuDTO.getId()));
    }

    @Override
    public boolean hasChild(String menuId) {
        return baseMapper.exists(Wrappers.<SysMenu>lambdaQuery()
                .eq(SysMenu::getParentId, menuId));
    }


    @Override
    public boolean checkMenuExistRole(String menuId) {
        return roleMenuLinkService.exists(Wrappers.<RoleMenuLink>lambdaQuery()
                .eq(RoleMenuLink::getMenuId, menuId));
    }

    @Override
    public boolean addMenu(SysMenuDTO sysMenuDTO) {
        SysMenu sysMenu = MapstructUtil.convert(sysMenuDTO, SysMenu.class);

        if (StringUtil.hasText(sysMenu.getParentId())) {
            SysMenu menu = baseMapper.selectOne(Wrappers.<SysMenu>lambdaQuery()
                    .eq(SysMenu::getMenuId, sysMenu.getParentId()));

            // 如果父节点不为正常状态,则不允许新增子节点
            if (!ColumnConstant.STATUS_NORMAL.equals(menu.getStatus())) {
                throw new ServiceException("菜单停用，不允许新增");
            }

            return baseMapper.insert(sysMenu) > 0;
        }

        sysMenu.setParentId(ColumnConstant.PARENT_ID);
        // 更新部分菜单数据到 Meta
        if (!sysMenu.getMenuType().equals(ColumnConstant.MENU_TYPE_BUTTON)) {
            Meta meta = sysMenu.getMeta();

            if (Objects.isNull(meta)) {
                meta = new Meta();
            }

            meta.setTitle(sysMenu.getMenuName())
                    .setIcon(sysMenu.getIcon())
                    .setRank(sysMenu.getOrderNum());
            sysMenu.setMeta(meta);
        }

        return baseMapper.insert(sysMenu) > 0;
    }

    @Override
    public boolean editMenu(SysMenuDTO sysMenuDTO) {
        SysMenu sysMenu = MapstructUtil.convert(sysMenuDTO, SysMenu.class);
        // 更新部分菜单数据到 Meta
        if (!sysMenu.getMenuType().equals(ColumnConstant.MENU_TYPE_BUTTON)) {
            Meta meta = sysMenu.getMeta();

            if (Objects.isNull(meta)) {
                meta = new Meta();
            }

            meta.setTitle(sysMenu.getMenuName())
                    .setIcon(sysMenu.getIcon())
                    .setRank(sysMenu.getOrderNum());
            sysMenu.setMeta(meta);
        }


        return baseMapper.updateById(sysMenu) > 0;
    }

    @Override
    public boolean removeMenu(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public boolean checkAppExitMenu(List<String> appIds) {
        return baseMapper.exists(Wrappers.<SysMenu>lambdaQuery()
                .in(SysMenu::getAppId, appIds));
    }

    @Override
    public Set<String> listMenuPermissionByUserId(String userId) {
        List<SysMenu> sysMenuList = baseMapper.listMenuListByUserId(TenantConstant.DEFAULT_UAC_APP_ID, userId, false);
        List<String> menuPerms = sysMenuList.stream().map(SysMenu::getPermission).toList();

        Set<String> permsSet = new HashSet<>();
        for (String perm : menuPerms) {
            if (StringUtil.hasText(perm)) {
                permsSet.addAll(List.of(perm.trim().split(",")));
            }
        }
        return permsSet;
    }
}




