package top.teek.ag.system.security.handler;

import top.teek.ag.core.constant.AuthConstant;
import top.teek.ag.core.event.LoginInfoEvent;
import top.teek.helper.SpringHelper;
import top.teek.security.domain.SecurityUser;
import top.teek.web.utils.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2022/12/10 23:45
 * @note 登录认证成功处理器类
 */
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 获取当登录用户信息
        SecurityUser user = (SecurityUser) authentication.getPrincipal();

        LoginInfoEvent loginInfoEvent = LoginInfoEvent.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .loginTime(LocalDateTime.now())
                .status(AuthConstant.LOGIN_SUCCESS)
                .request(ServletUtil.getRequest())
                .message("登录成功")
                .build();
        // 发布登录成功事件
        SpringHelper.publishEvent(loginInfoEvent);
    }
}
