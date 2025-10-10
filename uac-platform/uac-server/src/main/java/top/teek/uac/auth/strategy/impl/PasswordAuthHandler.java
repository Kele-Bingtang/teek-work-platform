package top.teek.uac.auth.strategy.impl;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import top.teek.core.validate.AuthGroup;
import top.teek.jwt.properties.JwtProperties;
import top.teek.security.JwtAuthenticationToken;
import top.teek.security.domain.LoginUser;
import top.teek.security.enumeration.AuthGrantTypeEnum;
import top.teek.security.utils.SecurityUtils;
import top.teek.tenant.helper.TenantHelper;
import top.teek.uac.auth.strategy.AuthHandler;
import top.teek.uac.core.bo.LoginSuccessBO;
import top.teek.uac.core.bo.LoginUserBO;
import top.teek.uac.core.constant.AuthConstant;
import top.teek.uac.core.helper.UacHelper;
import top.teek.uac.system.listen.LoginEventListen;
import top.teek.uac.system.model.po.SysClient;
import top.teek.uac.system.security.handler.LoginFailureHandler;
import top.teek.uac.system.security.handler.LoginSuccessHandler;
import top.teek.utils.AddressUtil;
import top.teek.utils.MapstructUtil;
import top.teek.web.utils.ServletUtil;
import top.teek.utils.ValidatorUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2023/11/14 0:14
 * @note 密码处理器
 */
@Slf4j
@RequiredArgsConstructor
@Service("password" + AuthHandler.BASE_NAME)
public class PasswordAuthHandler implements AuthHandler {
    
    private final JwtProperties jwtProperties;
    private final AuthenticationManager authenticationManager;
    private final LoginEventListen loginEventListen;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;

    @Override
    public boolean support(AuthGrantTypeEnum grantType) {
        return grantType.equals(AuthGrantTypeEnum.PASSWORD);
    }

    @Override
    public void validate(LoginUserBO loginUserBO) {
        ValidatorUtil.validate(loginUserBO, AuthGroup.PasswordGroup.class);
    }

    @Override
    public LoginSuccessBO login(LoginUserBO loginUserBO, SysClient sysClient) {
        // 检查用户是否被锁定登录
        loginEventListen.checkLogin(loginUserBO);

        Long timeout = sysClient.getTimeout();

        if (Objects.isNull(timeout)) {
            timeout = jwtProperties.getExpireTime();
        } else {
            timeout = timeout * 1000;
        }

        loginUserBO.setClientName(sysClient.getClientName());

        JwtAuthenticationToken token = new JwtAuthenticationToken(null, null);
        HttpServletRequest request = ServletUtil.getRequest();
        try {
            token = SecurityUtils.login(request,
                    TenantHelper.isEnable() ? loginUserBO.getTenantId() + ":" + loginUserBO.getUsername() : loginUserBO.getUsername(),
                    loginUserBO.getPassword(),
                    authenticationManager,
                    timeout);

            // 走了自定义认证，则 Spring Security 不会调用自定义的成功处理器，这里需要手动调用
            // 参考 org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter.doFilter()
            if (Objects.nonNull(request)) {
                request.setAttribute(AuthConstant.CLIENT_NAME, loginUserBO.getClientName());
            }
            loginSuccessHandler.onAuthenticationSuccess(request, ServletUtil.getResponse(), token.getAuthentication());
        } catch (InternalAuthenticationServiceException exception) {
            // 走了自定义认证，则 Spring Security 不会调用自定义的失败处理器，这里需要手动调用
            log.error("An internal error occurred while trying to authenticate the user.", exception);
            loginFailureHandler.onAuthenticationFailure(request, ServletUtil.getResponse(), exception, loginUserBO);
        } catch (AuthenticationException exception) {
            loginFailureHandler.onAuthenticationFailure(request, ServletUtil.getResponse(), exception, loginUserBO);
        }

        // 判断是否登录过且没过期。如果用户在浏览器 A 登录后，在浏览器 B 登录时，直接返回浏览器 A 登录的 token
        LoginUser loginUser = UacHelper.getLoginUser(loginUserBO.getUsername());

        LoginSuccessBO loginSuccessBO = new LoginSuccessBO();
        if (Objects.nonNull(loginUser)) {
            loginSuccessBO.setAccessToken(loginUser.getAccessToken());
            loginSuccessBO.setRefreshToken(loginUser.getRefreshToken());
            loginSuccessBO.setExpireIn(timeout);
        } else {
            loginSuccessBO.setAccessToken(token.getAccessToken());
            loginSuccessBO.setRefreshToken(token.getRefreshToken());
            loginSuccessBO.setExpireIn(timeout);
            // 用户基本信息存入 Redis
            cacheLoginUser(token, timeout, sysClient.getClientName(), request);
        }
        return loginSuccessBO;
    }

    /**
     * 用户基本信息存入 Redis
     *
     * @param token      认证信息令牌
     * @param timeout    超时时间
     * @param clientName 客户端名称
     * @param request    请求
     */
    private void cacheLoginUser(JwtAuthenticationToken token, Long timeout, String clientName, HttpServletRequest request) {
        LoginUser loginUser = MapstructUtil.convert(token.getAuthentication().getPrincipal(), LoginUser.class);

        // 获取 IP
        String clientIp = ServletUtil.getClientIp(request);
        String address = AddressUtil.getRealAddressByIp(clientIp);
        // 获取客户端标识
        UserAgent userAgent = UserAgentUtil.parse(ServletUtil.getUserAgent(request));
        // 获取客户端操作系统
        String os = userAgent.getOs().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        loginUser.setAccessToken(token.getAccessToken())
                .setRefreshToken(token.getRefreshToken())
                .setLoginIp(clientIp)
                .setLoginLocation(address)
                .setOs(os)
                .setBrowser(browser)
                .setClientName(clientName)
                .setLoginTime(LocalDateTime.now());

        UacHelper.cacheUserInfo(loginUser, timeout);
    }
}
