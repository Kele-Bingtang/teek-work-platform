package top.teek.ag.system.service;

import top.teek.ag.system.model.dto.ServiceInfoDTO;
import top.teek.ag.system.model.po.ServiceInfo;
import top.teek.ag.system.model.vo.ServiceInfoVO;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Teeker
 * @date 2024-06-22 00:15:48
 * @note 针对表「t_service（服务表）」的数据库操作 Service
 */
public interface ServiceInfoService extends IService<ServiceInfo> {

    /**
     * 根据服务 ID 查询服务信息
     *
     * @param serviceId 服务 ID
     * @return 服务信息
     */
    ServiceInfoVO getByServiceId(String serviceId);

    /**
     * 分页查询服务列表（分页）
     *
     * @param serviceInfoDTO 服务信息
     * @param pageQuery      分页查询
     * @return 服务列表
     */
    TablePage<ServiceInfoVO> listPage(ServiceInfoDTO serviceInfoDTO, PageQuery pageQuery);

    /**
     * 根据项目 ID 查询服务列表
     *
     * @param projectId 项目 ID
     * @param serviceId 服务 ID
     * @return 服务列表
     */
    List<ServiceInfoVO> listSelectInProject(String projectId, String serviceId);
    
    /**
     * 新增服务
     *
     * @param serviceInfoDTO 服务信息
     * @return 是否新增成功
     */
    boolean addService(ServiceInfoDTO serviceInfoDTO);

    /**
     * 修改服务
     *
     * @param serviceInfoDTO 服务信息
     * @return 是否修改成功
     */
    boolean editService(ServiceInfoDTO serviceInfoDTO);

    /**
     * 删除服务
     *
     * @param serviceId 服务 ID
     * @return 是否删除成功
     */
    boolean removeService(String serviceId);

    /**
     * 根据分类 ID 删除所有服务
     *
     * @param categoryId 分类 ID
     * @return 是否删除成功
     */
    boolean removeAllServiceInfo(String categoryId);

    /**
     * 根据项目 ID 删除所有服务
     *
     * @param projectId 项目 ID
     * @return 是否删除成功
     */
    boolean removeAllServiceInfoByProjectId(String projectId);

    /**
     * 根据团队 ID 删除所有服务
     *
     * @param teamId 团队 ID
     * @return 是否删除成功
     */
    boolean removeAllServiceInfoByTeamId(String teamId);

    /**
     * 检查服务名称是否唯一
     *
     * @param serviceInfoDTO 服务信息
     * @return 是否唯一
     */
    boolean checkServiceNameUnique(ServiceInfoDTO serviceInfoDTO);

    /**
     * 检查服务 URL 是否唯一
     *
     * @param serviceInfoDTO 服务信息
     * @return 是否唯一
     */
    boolean checkServiceUrlUnique(ServiceInfoDTO serviceInfoDTO);

    /**
     * 生成列配置项
     *
     * @param serviceInfoDTO 服务信息
     * @return 成功失败信息
     */
    Integer generateCol(ServiceInfoDTO serviceInfoDTO);

}
