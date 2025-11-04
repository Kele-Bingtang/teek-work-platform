package top.teek.uac.system.service.impl;

import cn.hutool.core.util.RandomUtil;
import top.teek.cache.helper.CacheHelper;
import top.teek.core.constants.ColumnConstant;
import top.teek.core.exception.ServiceException;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.core.constant.CacheNameConstant;
import top.teek.uac.core.constant.TenantConstant;
import top.teek.uac.system.mapper.SysTenantMapper;
import top.teek.uac.system.model.dto.SysTenantDTO;
import top.teek.uac.system.model.po.*;
import top.teek.uac.system.model.vo.SysTenantVO;
import top.teek.uac.system.service.*;
import top.teek.uac.system.model.po.*;
import top.teek.utils.ListUtil;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.teek.uac.system.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_sys_tenant(租户表)】的数据库操作Service实现
 */
@Service
@RequiredArgsConstructor
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements SysTenantService {

    private final ReentrantLock reentrantLock = new ReentrantLock();
    private final PasswordEncoder passwordEncoder;
    private final SysUserService sysUserService;
    private final SysRoleService sysRoleService;
    private final SysDeptService sysDeptService;
    private final SysMenuService sysMenuService;
    private final RoleMenuLinkService roleMenuLinkService;
    private final RoleDeptLinkService roleDeptLinkService;
    private final UserRoleLinkService userRoleLinkService;
    private final SysAppService sysAppService;
    private final SysDictTypeService sysDictTypeService;
    private final SysDictDataService sysDictDataService;

    @Override
    @Cacheable(cacheNames = CacheNameConstant.SYS_TENANT, key = "#tenantId")
    public SysTenant queryByTenantId(String tenantId) {
        return baseMapper.selectOne(Wrappers.<SysTenant>lambdaQuery().eq(SysTenant::getTenantId, tenantId));
    }

    @Override
    public List<SysTenantVO> listAll(SysTenantDTO sysTenantDTO) {
        LambdaQueryWrapper<SysTenant> wrapper = buildQueryWrapper(sysTenantDTO);
        List<SysTenant> sysTenantList = baseMapper.selectList(wrapper);

        return MapstructUtil.convert(sysTenantList, SysTenantVO.class);
    }

    @Override
    public TablePage<SysTenantVO> listPage(SysTenantDTO sysTenantDTO, PageQuery pageQuery) {
        LambdaQueryWrapper<SysTenant> wrapper = buildQueryWrapper(sysTenantDTO);
        Page<SysTenant> sysTenantPage = baseMapper.selectPage(pageQuery.buildPage(), wrapper);

        return TablePage.build(sysTenantPage, SysTenantVO.class);
    }

    private LambdaQueryWrapper<SysTenant> buildQueryWrapper(SysTenantDTO sysTenantDTO) {
        return Wrappers.<SysTenant>lambdaQuery()
                .eq(StringUtil.hasText(sysTenantDTO.getTenantId()), SysTenant::getTenantId, sysTenantDTO.getTenantId())
                .eq(Objects.nonNull(sysTenantDTO.getUserCount()), SysTenant::getUserCount, sysTenantDTO.getUserCount())
                .eq(Objects.nonNull(sysTenantDTO.getStatus()), SysTenant::getStatus, sysTenantDTO.getStatus())
                .orderByAsc(SysTenant::getId);
    }

    @Override
    public boolean checkCompanyNameUnique(SysTenantDTO sysTenantDTO) {
        return baseMapper.exists(new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getTenantName, sysTenantDTO.getTenantName())
                .ne(Objects.nonNull(sysTenantDTO.getId()), SysTenant::getId, sysTenantDTO.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addTenant(SysTenantDTO sysTenantDTO) {
        SysTenant sysTenant = MapstructUtil.convert(sysTenantDTO, SysTenant.class);
        // 获取数据库所有的租户 ID，然后根据最后一个生成新的 ID
        List<SysTenant> sysTenantList = baseMapper.selectList(Wrappers.<SysTenant>lambdaQuery().select(SysTenant::getTenantId));

        reentrantLock.lock();
        boolean result;
        String tenantId;
        try {
            List<String> tenantIds = sysTenantList.stream().filter(Objects::nonNull).map(t -> String.valueOf(t.getTenantId())).toList();
            // 生成新的租户 ID
            tenantId = generateTenantId(tenantIds);
            sysTenant.setTenantId(tenantId);
            result = baseMapper.insert(sysTenant) > 0;
        } finally {
            reentrantLock.unlock();
        }
        if (!result) {
            throw new ServiceException("创建租户失败");
        }

        // 创建应用
        String appId = createApp(tenantId);

        // 创建角色
        String roleId = createRole(tenantId, appId);

        // 创建菜单和角色菜单关联，取默认租户的菜单
        createMenuAndRoleMenuLink(tenantId, appId, roleId);

        // 创建部门: 企业名称是部门名称，创建角色和部门关联
        SysDept sysDept = createDeptAndRoleDeptLink(tenantId, appId, sysTenantDTO.getTenantName(), roleId);

        // 创建系统用户，创建用户角色关联
        SysUser sysUser = createUserAndUserRoleLink(sysTenantDTO, tenantId, appId, sysDept.getDeptId(), roleId);

        // 新增系统用户后，默认当前用户为部门的负责人
        SysDept dept = new SysDept();
        dept.setId(sysDept.getId());
        dept.setLeader(sysUser.getUserId());
        sysDeptService.updateById(dept);

        // 创建字典类型
        createDictType(tenantId, appId);

        // 创建字典数据
        createDictData(tenantId, appId);

        return true;
    }

    private String generateTenantId(List<String> tenantIds) {
        // 随机生成6位
        String numbers = RandomUtil.randomNumbers(6);
        // 判断是否存在，如果存在则重新生成
        if (tenantIds.contains(numbers)) {
            generateTenantId(tenantIds);
        }
        return numbers;
    }

    /**
     * 创建应用
     *
     * @param tenantId 租户 ID
     * @return 应用 ID
     */
    private String createApp(String tenantId) {
        SysApp sysApp = sysAppService.getOne(Wrappers.<SysApp>lambdaQuery()
                .eq(SysApp::getAppId, TenantConstant.DEFAULT_UAC_APP_ID)
                .eq(SysApp::getTenantId, TenantConstant.DEFAULT_TENANT_ID));

        SysApp app = new SysApp()
                .setAppId(TenantConstant.DEFAULT_UAC_APP_ID)
                .setAppName(sysApp.getAppName())
                .setAppCode(sysApp.getAppCode())
                .setIntro(sysApp.getIntro())
                .setOrderNum(sysApp.getOrderNum())
                .setOwnerId(sysApp.getOwnerId())
                .setOwnerName(sysApp.getOwnerName())
                .setClientId(sysApp.getClientId())
                .setTenantId(tenantId);

        sysAppService.save(app);

        return app.getAppId();
    }

    /**
     * 创建角色，创建角色菜单，取默认租户的菜单
     *
     * @param tenantId 租户ID
     * @return 角色 ID
     */
    private String createRole(String tenantId, String appId) {
        // 创建角色
        SysRole sysRole = new SysRole();
        sysRole.setTenantId(tenantId);
        sysRole.setAppId(appId);
        sysRole.setRoleId(TenantConstant.DEFAULT_ROLE_ID);
        sysRole.setRoleCode(TenantConstant.TENANT_ADMIN_ROLE_KEY);
        sysRole.setRoleName(TenantConstant.TENANT_ADMIN_ROLE_NAME);
        sysRole.setOrderNum(1);
        sysRole.setStatus(ColumnConstant.STATUS_NORMAL);
        sysRoleService.save(sysRole);

        return sysRole.getRoleId();
    }

    /**
     * 创建菜单和角色菜单关联，取默认租户的菜单
     *
     * @param tenantId 租户 ID
     * @param appId    应用 ID
     * @param roleId   角色 ID
     */
    private void createMenuAndRoleMenuLink(String tenantId, String appId, String roleId) {
        List<SysMenu> sysMenuList = sysMenuService.list(Wrappers.<SysMenu>lambdaQuery()
                .eq(SysMenu::getAppId, TenantConstant.DEFAULT_UAC_APP_ID)
                .eq(SysMenu::getTenantId, TenantConstant.DEFAULT_TENANT_ID)
        );

        List<SysMenu> menuList = ListUtil.newArrayList(sysMenuList, sysMenu -> {
                    sysMenu.setId(null);
                    sysMenu.setCreateTime(null);
                    sysMenu.setUpdateTime(null);
                    sysMenu.setTenantId(tenantId);
                    sysMenu.setAppId(appId);
                    return sysMenu;
                }
        );

        Db.saveBatch(menuList);

        // 创建角色菜单，取默认租户的菜单
        List<String> menuIds = roleMenuLinkService.listMenuIdsByRoleId(TenantConstant.DEFAULT_ROLE_ID, TenantConstant.DEFAULT_UAC_APP_ID, TenantConstant.DEFAULT_TENANT_ID);

        List<RoleMenuLink> userGroupLinkList = ListUtil.newArrayList(menuIds, menuId ->
                        new RoleMenuLink().setMenuId(menuId)
                                .setRoleId(roleId)
                                .setTenantId(tenantId)
                                .setAppId(appId)
                , RoleMenuLink.class);

        Db.saveBatch(userGroupLinkList);
    }

    /**
     * 创建部门: 企业名称是部门名称，创建角色和部门关联
     *
     * @param tenantId 租户ID
     * @param deptName 部门名称
     * @param roleId   角色 ID
     * @return 创建的部门信息
     */
    private SysDept createDeptAndRoleDeptLink(String tenantId, String appId, String deptName, String roleId) {
        // 创建部门: 企业名称是部门名称
        SysDept sysDept = new SysDept()
                .setTenantId(tenantId)
                .setDeptName(deptName)
                .setParentId(ColumnConstant.PARENT_ID)
                .setAncestors(ColumnConstant.PARENT_ID);

        sysDeptService.save(sysDept);

        RoleDeptLink roleDeptLink = new RoleDeptLink()
                .setRoleId(roleId)
                .setDeptId(sysDept.getDeptId())
                .setAppId(appId)
                .setTenantId(tenantId);

        roleDeptLinkService.save(roleDeptLink);

        return sysDept;
    }

    /**
     * 创建系统用户，创建用户角色关联
     *
     * @param sysTenantDTO 租户创建信息
     * @param tenantId     租户 ID
     * @param deptId       部门 ID
     * @param roleId       角色 ID
     * @return 创建的用户信息
     */
    private SysUser createUserAndUserRoleLink(SysTenantDTO sysTenantDTO, String tenantId, String appId, String deptId, String roleId) {
        SysUser sysUser = new SysUser()
                .setUserId(TenantConstant.DEFAULT_USER_ID)
                .setUsername(sysTenantDTO.getUsername())
                .setNickname(sysTenantDTO.getUsername())
                .setPassword(passwordEncoder.encode(sysTenantDTO.getPassword()))
                .setTenantId(tenantId)
                .setDeptId(deptId);

        sysUserService.save(sysUser);

        // 用户和角色关联
        UserRoleLink userRoleLink = new UserRoleLink()
                .setUserId(sysUser.getUserId())
                .setRoleId(roleId)
                .setValidFrom(LocalDate.now())
                .setExpireOn(LocalDate.now().plusYears(99))
                .setAppId(appId)
                .setTenantId(tenantId);
        userRoleLinkService.save(userRoleLink);

        return sysUser;
    }

    /**
     * 创建字典类型
     *
     * @param tenantId 租户 ID
     */
    private void createDictType(String tenantId, String appId) {
        List<SysDictType> sysDictTypeList = sysDictTypeService.list(Wrappers.<SysDictType>lambdaQuery()
                .eq(SysDictType::getAppId, TenantConstant.DEFAULT_UAC_APP_ID)
                .eq(SysDictType::getTenantId, TenantConstant.DEFAULT_TENANT_ID)
        );

        List<SysDictType> dictTypeList = ListUtil.newArrayList(sysDictTypeList, sysDictType ->
                new SysDictType()
                        .setDictName(sysDictType.getDictName())
                        .setDictCode(sysDictType.getDictCode())
                        .setIsCascade(sysDictType.getIsCascade())
                        .setIntro(sysDictType.getIntro())
                        .setTenantId(tenantId)
                        .setAppId(appId)

        );

        sysDictTypeService.saveBatch(dictTypeList);
    }

    /**
     * 创建字典数据
     *
     * @param tenantId 租户 ID
     */
    private void createDictData(String tenantId, String appId) {
        List<SysDictData> sysDictDataList = sysDictDataService.list(Wrappers.<SysDictData>lambdaQuery()
                .eq(SysDictData::getAppId, TenantConstant.DEFAULT_UAC_APP_ID)
                .eq(SysDictData::getTenantId, TenantConstant.DEFAULT_TENANT_ID)
        );

        List<SysDictData> dictDataList = ListUtil.newArrayList(sysDictDataList, sysDictData ->
                new SysDictData()
                        .setDataId(sysDictData.getDataId())
                        .setDictCode(sysDictData.getDictCode())
                        .setParentId(sysDictData.getParentId())
                        .setDictValue(sysDictData.getDictValue())
                        .setDictLabel(sysDictData.getDictLabel())
                        .setDictSort(sysDictData.getDictSort())
                        .setTagEl(sysDictData.getTagEl())
                        .setTagType(sysDictData.getTagType())
                        .setTagEffect(sysDictData.getTagEffect())
                        .setTagAttributes(sysDictData.getTagAttributes())
                        .setIsDefault(sysDictData.getIsDefault())
                        .setTenantId(tenantId)
                        .setAppId(appId)

        );

        sysDictDataService.saveBatch(dictDataList);
    }

    @Override
    @CacheEvict(cacheNames = CacheNameConstant.SYS_TENANT, key = "#sysTenantDTO.tenantId")
    public boolean editTenant(SysTenantDTO sysTenantDTO) {
        checkTenantAllowed(sysTenantDTO.getTenantId());
        SysTenant sysTenant = MapstructUtil.convert(sysTenantDTO, SysTenant.class);
        return baseMapper.updateById(sysTenant) > 0;
    }

    @Override
    public boolean removeBatch(List<Long> ids) {
        List<SysTenant> sysTenantList = baseMapper.selectBatchIds(ids);

        boolean result = baseMapper.deleteBatchIds(ids) > 0;
        
        for (SysTenant sysTenant : sysTenantList) {
            CacheHelper.evict(CacheNameConstant.SYS_TENANT, sysTenant.getTenantId());
        }

        return result;
    }

    @Override
    public void checkTenantAllowed(String tenantId) {
        if (Objects.nonNull(tenantId) && TenantConstant.DEFAULT_TENANT_ID.equals(tenantId)) {
            throw new ServiceException("不允许操作默认管理租户");
        }
    }
}




