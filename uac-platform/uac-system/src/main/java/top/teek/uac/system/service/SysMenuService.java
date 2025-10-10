package top.teek.uac.system.service;

import cn.hutool.core.lang.tree.Tree;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.system.model.dto.SysMenuDTO;
import top.teek.uac.system.model.po.SysMenu;
import top.teek.uac.system.model.vo.SysMenuVO;
import top.teek.uac.system.model.vo.router.RouterVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_sys_menu(菜单表)】的数据库操作Service
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 查询前端需要的路由列表
     *
     * @param appId 应用 ID
     * @return 路由列表
     */
    List<RouterVO> listRoutes(String appId);

    /**
     * 通过条件查询菜单列表
     *
     * @param sysMenuDTO 查询条件
     * @return 菜单列表
     */
    List<SysMenuVO> listAll(SysMenuDTO sysMenuDTO);

    /**
     * 通过条件查询菜单列表（支持分页）
     *
     * @param sysMenuDTO 查询条件
     * @param pageQuery  分页参数
     * @return 菜单列表
     */
    TablePage<SysMenuVO> listPage(SysMenuDTO sysMenuDTO, PageQuery pageQuery);

    /**
     * 查询菜单下拉框列表
     *
     * @param sysMenuDTO 查询条件
     * @return 菜单下拉框列表
     */
    List<Tree<String>> listMenuTreeSelect(SysMenuDTO sysMenuDTO);

    /**
     * 查询菜单树形表格列表
     *
     * @param sysMenuDTO 查询条件
     * @return 菜单树形表格列表
     */
    List<SysMenuVO> listMenuTreeTable(SysMenuDTO sysMenuDTO);
    
    /**
     * 获取用户的菜单权限
     *
     * @param appId 应用 ID
     * @param userId 用户 ID
     * @return 权限列表
     */
    List<SysMenuVO> listMenuListByUserId(String appId,  String userId);

    /**
     * 通过角色 ID 查询菜单列表
     *
     * @param roleId 角色 ID
     * @param appId  应用 ID
     * @return 菜单列表
     */
    List<Tree<String>> listMenuListByRoleId(String roleId, String appId);
    
    /**
     * 检查菜单名称是否唯一
     *
     * @param sysMenuDTO 查询条件
     * @return 是否唯一
     */
    boolean checkMenuNameUnique(SysMenuDTO sysMenuDTO);

    /**
     * 检查菜单是否存在子菜单
     *
     * @param menuId 菜单id
     * @return 是否存在子菜单
     */
    boolean hasChild(String menuId);

    /**
     * 校验菜单编码是否唯一
     *
     * @param sysMenuDTO 查询条件
     * @return 是否唯一
     */
    boolean checkMenuCodeUnique(SysMenuDTO sysMenuDTO);
    
    /**
     * 检查菜单是否存在角色关联
     *
     * @param menuId 菜单 ID
     * @return 是否存在角色关联
     */
    boolean checkMenuExistRole(String menuId);

    /**
     * 新增菜单
     *
     * @param sysMenuDTO 新增对象
     * @return 是否成功
     */
    boolean addMenu(SysMenuDTO sysMenuDTO);

    /**
     * 修改菜单
     *
     * @param sysMenuDTO 修改对象
     * @return 是否成功
     */
    boolean editMenu(SysMenuDTO sysMenuDTO);

    /**
     * 删除菜单
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean removeMenu(Long id);

    /**
     * 校验 App 下是否存在菜单
     *
     * @param appIds 应用 ID 列表
     * @return 是否存在
     */
    boolean checkAppExitMenu(List<String> appIds);

    /**
     * 根据用户 ID 查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> listMenuPermissionByUserId(String userId);
}
