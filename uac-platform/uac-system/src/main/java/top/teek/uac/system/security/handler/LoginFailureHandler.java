package top.teek.uac.system.security.handler;

import top.teek.core.base.BaseCommonEnum;
import top.teek.uac.core.event.LoginInfoEvent;
import top.teek.core.http.HttpResult;
import top.teek.helper.SpringHelper;
import top.teek.security.enumeration.AuthErrorCodeEnum;
import top.teek.uac.core.bo.LoginUserBO;
import top.teek.uac.core.constant.AuthConstant;
import top.teek.utils.JacksonUtil;
import top.teek.web.utils.ServletUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Teeker
 * @date 2022/12/10 23:51
 * @note 用户认证失败处理类
 */
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception, LoginUserBO loginUserBO) {
        // 判断异常类型
        BaseCommonEnum authErrorCodeEnum = AuthErrorCodeEnum.LOGIN_FAIL;
        if (exception instanceof AccountExpiredException) {
            authErrorCodeEnum = AuthErrorCodeEnum.USER_ACCOUNT_EXPIRED;
        } else if (exception instanceof BadCredentialsException) {
            authErrorCodeEnum = AuthErrorCodeEnum.USERNAME_PASSWORD_ERROR;
        } else if (exception instanceof CredentialsExpiredException) {
            authErrorCodeEnum = AuthErrorCodeEnum.USER_PASSWORD_EXPIRED;
        } else if (exception instanceof DisabledException) {
            authErrorCodeEnum = AuthErrorCodeEnum.USER_ACCOUNT_DISABLE;
        } else if (exception instanceof LockedException) {
            authErrorCodeEnum = AuthErrorCodeEnum.USER_ACCOUNT_LOCKED;
        } else if (exception instanceof InternalAuthenticationServiceException) {
            authErrorCodeEnum = AuthErrorCodeEnum.USER_ACCOUNT_NOT_EXIST;
        }
        log.error("Exception：{}", authErrorCodeEnum.getMessage());
        // 该异常是在 UserDetailsService.loadUserByUsername 里抛出的，不需要记录
        if (!(exception instanceof InternalAuthenticationServiceException)) {
            LoginInfoEvent loginInfoEvent = LoginInfoEvent.builder()
                    .tenantId(loginUserBO.getTenantId())
                    .username(loginUserBO.getUsername())
                    .clientName(loginUserBO.getClientName())
                    .status(AuthConstant.LOGIN_FAIL)
                    .request(ServletUtil.getRequest())
                    .build();
            loginInfoEvent.setMessage(authErrorCodeEnum.getMessage());
            // 发布登录失败事件
            SpringHelper.publishEvent(loginInfoEvent);
        }

        // 设置客户端响应编码格式
        response.setContentType("application/json;charset=UTF-8");
        // 获取输出流
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
        } catch (IOException e) {
            log.error("获取输出流失败：{}", e.getMessage());
        }
        // 将错误信息转换成 JSON
        writer.println(JacksonUtil.toJsonStr(HttpResult.response(null, authErrorCodeEnum)));
        writer.flush();
        writer.close();
    }

    /**
     * Spring Security 失败默认走的方法
     * 因为使用了 JWT，所以该方法不会被调用，需要手动调用
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        
    }
}