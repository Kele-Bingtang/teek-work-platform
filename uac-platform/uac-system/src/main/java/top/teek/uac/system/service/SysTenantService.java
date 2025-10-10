package top.teek.uac.system.service;

import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.system.model.dto.SysTenantDTO;
import top.teek.uac.system.model.po.SysTenant;
import top.teek.uac.system.model.vo.SysTenantVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_sys_tenant(租户表)】的数据库操作Service
 */
public interface SysTenantService extends IService<SysTenant> {

    /**
     * 根据租户 ID 查询租户信息
     *
     * @param tenantId 租户 ID
     * @return 租户信息
     */
    SysTenant queryByTenantId(String tenantId);

    /**
     * 通过条件查询租户列表
     *
     * @param sysTenantDTO 查询条件
     * @return 租户列表
     */
    List<SysTenantVO> listAll(SysTenantDTO sysTenantDTO);

    /**
     * 通过条件查询租户列表
     *
     * @param sysTenantDTO 查询条件
     * @param pageQuery    分页参数
     * @return 租户列表
     */
    TablePage<SysTenantVO> listPage(SysTenantDTO sysTenantDTO, PageQuery pageQuery);

    /**
     * 新增租户
     *
     * @param sysTenantDTO 租户信息
     * @return 是否新增成功
     */
    boolean addTenant(SysTenantDTO sysTenantDTO);

    /**
     * 更新租户
     *
     * @param sysTenantDTO 租户信息
     * @return 是否更新成功
     */
    boolean editTenant(SysTenantDTO sysTenantDTO);

    /**
     * 删除租户
     *
     * @param ids 主键列表
     * @return 是否删除成功
     */
    boolean removeBatch(List<Long> ids);

    /**
     * 校验租户名称是否唯一
     *
     * @param sysTenantDTO 租户信息
     * @return 是否唯一
     */
    boolean checkCompanyNameUnique(SysTenantDTO sysTenantDTO);

    /**
     * 校验租户是否允许操作
     */
    void checkTenantAllowed(String tenantId);

}
