package top.teek.ag.system.service;

import top.teek.ag.core.event.LoginInfoEvent;
import top.teek.ag.system.model.po.LoginLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Teeker
* @date 2024-06-22 16:27:53
 * @note 针对表「t_login_log（操作日志记录）」的数据库操作 Service
 */
public interface LoginLogService extends IService<LoginLog> {

    /**
     * 记录登录日志
     *
     * @param loginInfoEvent 登录事件信息
     */
    void recordLoginLog(LoginInfoEvent loginInfoEvent);
}
