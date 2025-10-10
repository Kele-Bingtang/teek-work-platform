package top.teek.uac.system.service;

import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.system.model.dto.SysRoleDTO;
import top.teek.uac.system.model.po.SysRole;
import top.teek.uac.system.model.vo.SysRoleVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_sys_role(角色信息表)】的数据库操作Service
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 通过条件查询角色列表
     *
     * @param sysRoleDTO 查询条件
     * @return 角色列表
     */
    List<SysRoleVO> listAll(SysRoleDTO sysRoleDTO);

    /**
     * 通过条件查询角色列表（支持分页）
     *
     * @param sysRoleDTO 查询条件
     * @param pageQuery  分页参数
     * @return 角色列表
     */
    TablePage<SysRoleVO> listPage(SysRoleDTO sysRoleDTO, PageQuery pageQuery);

    /**
     * 校验角色编码是否唯一
     *
     * @param sysRoleDTO 角色信息
     * @return 是否唯一
     */
    boolean checkRoleCodeUnique(SysRoleDTO sysRoleDTO);

    /**
     * 校验角色名称是否唯一
     *
     * @param sysRoleDTO 角色信息
     * @return 是否唯一
     */
    boolean checkRoleNameUnique(SysRoleDTO sysRoleDTO);

    /**
     * 新增角色
     *
     * @param sysRoleDTO 角色信息
     * @return 是否成功
     */
    boolean addRole(SysRoleDTO sysRoleDTO);

    /**
     * 更新角色
     *
     * @param sysRoleDTO 角色信息
     * @return 是否成功
     */
    boolean editRole(SysRoleDTO sysRoleDTO);

    /**
     * 批量删除角色
     *
     * @param ids 主键列表
     * @return 是否成功
     */
    boolean removeBatch(List<Long> ids, List<String> roleIds);

    /**
     * 校验 App 下是否存在角色
     *
     * @param appIds 应用 ID 列表
     * @return 是否存在
     */
    boolean checkAppExitRole(List<String> appIds);

    /**
     * 校验角色是否允许操作
     * @param sysRoleDTO 角色信息
     */
    void checkRoleAllowed(SysRoleDTO sysRoleDTO);

    /**
     * 根据用户 ID 查询角色码
     *
     * @param userId 用户 ID
     * @return 权限列表
     */
    Set<String> listRoleCodesByUserId(String userId);
}
