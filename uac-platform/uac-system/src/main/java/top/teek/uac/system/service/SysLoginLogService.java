package top.teek.uac.system.service;

import top.teek.uac.core.event.LoginInfoEvent;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.system.model.dto.SysLoginLogDTO;
import top.teek.uac.system.model.po.SysLoginLog;
import top.teek.uac.system.model.vo.SysLoginLogVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_sys_login_info(操作日志记录)】的数据库操作Service
 */
public interface SysLoginLogService extends IService<SysLoginLog> {

    /**
     * 记录登录日志
     *
     * @param loginInfoEvent 登录事件信息
     */
    void recordLoginLog(LoginInfoEvent loginInfoEvent);

    /**
     * 通过条件查询登录日志清单列表
     *
     * @param sysLoginLogDTO 登录信息查询条件
     * @return 登录信息清单
     */
    List<SysLoginLogVO> listAll(SysLoginLogDTO sysLoginLogDTO);

    /**
     * 通过条件查询登录日志列表（支持分页）
     *
     * @param sysLoginLogDTO 查询条件
     * @param pageQuery      分页条件
     * @return 登录日志列表
     */
    TablePage<SysLoginLogVO> listPage(SysLoginLogDTO sysLoginLogDTO, PageQuery pageQuery);

    /**
     * 批量删除登录日志
     *
     * @param ids 登录日志id列表
     * @return 是否删除成功
     */
    Boolean removeBatch(List<Long> ids);

    /**
     * 清空所有登录日志
     *
     * @return 是否清空成功
     */
    Boolean cleanAllOperaLog();

}
