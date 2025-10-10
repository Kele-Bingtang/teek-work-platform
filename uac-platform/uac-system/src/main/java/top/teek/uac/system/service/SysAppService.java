package top.teek.uac.system.service;

import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.system.model.dto.SysAppDTO;
import top.teek.uac.system.model.po.SysApp;
import top.teek.uac.system.model.vo.SysAppVO;
import top.teek.uac.system.model.vo.extra.AppTreeVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_sys_app(应用表)】的数据库操作Service
 */
public interface SysAppService extends IService<SysApp> {
    /**
     * 检查应用 ID 是否存在，如果存在则返回应用信息
     *
     * @param appId 应用 ID
     * @return 应用信息
     */
    SysApp checkAppIdThenGet(String tenantId,String appId);

    /**
     * 通过条件查询 App 清单列表
     *
     * @param sysAppDTO 应用信息查询条件
     * @return 应用信息清单
     */
    List<SysAppVO> listAll(SysAppDTO sysAppDTO);

    /**
     * 通过条件查询 App 清单列表
     *
     * @param sysAppDTO 应用信息查询条件
     * @param pageQuery 分页查询条件
     * @return 应用信息清单
     */
    TablePage<SysAppVO> listPage(SysAppDTO sysAppDTO, PageQuery pageQuery);

    /**
     * 新增一条应用信息
     *
     * @param sysAppDTO 应用信息
     * @return 是否成功
     */
    boolean addApp(SysAppDTO sysAppDTO);

    /**
     * 更新一条应用信息
     *
     * @param sysAppDTO 应用信息
     * @return 是否成功
     */
    boolean editApp(SysAppDTO sysAppDTO);

    /**
     * 批量删除应用信息
     *
     * @param ids 应用 ID 清单
     * @return 是否成功
     */
    boolean removeBatch(List<Long> ids);

    /**
     * 获取应用树形清单
     *
     * @return 应用树形清单
     */
    List<AppTreeVO> listTreeList();

    /**
     * 检查应用是否存在
     *
     * @param clientIds 客户端 ID 清单
     * @return 是否存在
     */
    boolean checkExitApp(List<String> clientIds);

    /**
     * 检查应用编码是否唯一
     * @param sysAppDTO 应用信息
     * @return 是否唯一
     */
    boolean checkAppCodeUnique(SysAppDTO sysAppDTO);
}
