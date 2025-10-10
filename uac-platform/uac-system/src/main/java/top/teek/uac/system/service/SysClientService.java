package top.teek.uac.system.service;

import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.system.model.dto.SysClientDTO;
import top.teek.uac.system.model.po.SysClient;
import top.teek.uac.system.model.vo.SysClientVO;
import top.teek.uac.system.model.vo.extra.ClientTreeVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

/**
 * @author Teeker
 * @date 2023-59-13 22:59:31
 * @note 针对表【t_sys_client(客户端授权表)】的数据库操作Service
 */
public interface SysClientService extends IService<SysClient> {

    /**
     * 检查客户端 ID 是否存在，如果存在则返回客户端信息
     *
     * @param clientId 客户端ID
     * @return 客户端信息
     */
    SysClient checkClientIdThenGet(String clientId);

    /**
     * 通过条件查询客户端信息
     *
     * @param sysClientDTO 客户端信息
     * @return 客户端信息
     */
    List<SysClientVO> listAll(SysClientDTO sysClientDTO);

    /**
     * 通过条件查询客户端信息
     *
     * @param sysClientDTO 客户端信息
     * @param pageQuery    分页参数
     * @return 客户端信息
     */
    TablePage<SysClientVO> listPage(SysClientDTO sysClientDTO, PageQuery pageQuery);

    /**
     * 查询客户端树形列表
     *
     * @return 客户端树形列表
     */
    List<ClientTreeVO> listTreeList();

    /**
     * 新增客户端信息
     *
     * @param sysClientDTO 客户端信息
     * @return 是否成功
     */
    boolean insertClient(SysClientDTO sysClientDTO);

    /**
     * 修改客户端信息
     *
     * @param sysClientDTO 客户端信息
     * @return 是否成功
     */
    boolean updateClient(SysClientDTO sysClientDTO);

    /**
     * 修改客户端状态
     *
     * @param id     主键
     * @param status 状态
     * @return 是否成功
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 删除客户端信息
     *
     * @param ids 主键
     * @return 是否成功
     */
    boolean removeBatch(Collection<Long> ids);

    /**
     * 检查客户端 key 是否唯一
     *
     * @param sysClientDTO 客户端信息
     * @return 是否唯一
     */
    boolean checkClientKeyUnique(SysClientDTO sysClientDTO);

    /**
     * 检查客户端 secret 是否唯一
     *
     * @param sysClientDTO 客户端信息
     * @return 是否唯一
     */
    boolean checkClientSecretUnique(SysClientDTO sysClientDTO);
}
