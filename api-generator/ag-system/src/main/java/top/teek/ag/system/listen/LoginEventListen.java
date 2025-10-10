package top.teek.ag.system.listen;

import cn.hutool.core.util.ObjectUtil;
import top.teek.ag.core.bo.LoginUserBO;
import top.teek.ag.core.constant.AuthConstant;
import top.teek.ag.core.event.LoginInfoEvent;
import top.teek.ag.core.exception.ApiGeneratorException;
import top.teek.ag.system.service.LoginLogService;
import top.teek.redis.constants.AuthRedisConstant;
import top.teek.redis.utils.RedisUtil;
import top.teek.web.utils.ServletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author Teeker
 * @date 2024/6/22 16:26:22
 * @note
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class LoginEventListen {
    @Value("${login.fail.maxRetryCount}")
    private Integer maxRetryCount;

    @Value("${login.fail.lockTime}")
    private Integer lockTime;

    private final LoginLogService loginLogService;

    @Async
    @EventListener
    public void listenerLoginInfo(LoginInfoEvent loginInfoEvent) {
        if (loginInfoEvent.getStatus().equals(AuthConstant.LOGIN_SUCCESS)) {
            tryClearErrorCount(loginInfoEvent);
        } else if (loginInfoEvent.getStatus().equals(AuthConstant.LOGIN_FAIL)) {
            recordErrorCount(loginInfoEvent);
        }
        loginLogService.recordLoginLog(loginInfoEvent);
    }

    /**
     * 解锁账号：累计的登录错误次数
     */
    public void tryClearErrorCount(LoginInfoEvent loginInfoEvent) {
        RedisUtil.delete(AuthRedisConstant.PWD_ERR_CNT_KEY + loginInfoEvent.getUsername());
    }

    /**
     * 记录累计的登录错误次数
     */
    public void recordErrorCount(LoginInfoEvent loginInfoEvent) {
        String errorKey = AuthRedisConstant.PWD_ERR_CNT_KEY + loginInfoEvent.getUsername();
        // 获取登录失败次数
        int errorCount = ObjectUtil.defaultIfNull((Integer) RedisUtil.getForValue(errorKey), 0);
        errorCount++;
        RedisUtil.setForValue(errorKey, errorCount, Duration.ofMinutes(lockTime));
        // 到达锁定次数阈值
        if (errorCount >= maxRetryCount) {
            String message = "密码输入错误 " + maxRetryCount + " 次，帐户锁定 " + lockTime + " 分钟";
            loginInfoEvent.setMessage(message);
            loginLogService.recordLoginLog(loginInfoEvent);
            throw new ApiGeneratorException(message);
        } else {
            String message = "密码输入错误 " + errorCount + " 次，还能输入 " + (maxRetryCount - errorCount) + " 次";
            loginInfoEvent.setMessage(message);
            loginLogService.recordLoginLog(loginInfoEvent);
            throw new ApiGeneratorException(message);
        }
    }

    public void checkLogin(LoginUserBO loginUserBO) {
        String errorKey = AuthRedisConstant.PWD_ERR_CNT_KEY + loginUserBO.getUsername();
        // 获取登录失败次数
        int errorCount = ObjectUtil.defaultIfNull((Integer) RedisUtil.getForValue(errorKey), 0);
        if (errorCount >= maxRetryCount) {
            String message = "密码输入错误 " + maxRetryCount + " 次，帐户锁定 " + lockTime + " 分钟";

            LoginInfoEvent loginInfoEvent = LoginInfoEvent.builder()
                    .userId("")
                    .username(loginUserBO.getUsername())
                    .status(AuthConstant.LOGIN_FAIL)
                    .request(ServletUtil.getRequest())
                    .build();

            loginInfoEvent.setMessage(message);
            loginLogService.recordLoginLog(loginInfoEvent);
            throw new ApiGeneratorException(message);
        }
    }
}
