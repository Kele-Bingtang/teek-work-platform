package top.teek.ag.auth.service;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import top.teek.ag.auth.model.dto.LoginUserDTO;
import top.teek.ag.auth.model.vo.LoginVO;
import top.teek.ag.core.bo.LoginUserBO;
import top.teek.ag.core.constant.AuthConstant;
import top.teek.ag.core.event.LoginInfoEvent;
import top.teek.ag.core.helper.AgHelper;
import top.teek.ag.system.listen.LoginEventListen;
import top.teek.ag.system.security.handler.LoginFailureHandler;
import top.teek.ag.system.security.handler.LoginSuccessHandler;
import top.teek.helper.SpringHelper;
import top.teek.jwt.properties.JwtProperties;
import top.teek.security.JwtAuthenticationToken;
import top.teek.security.domain.LoginUser;
import top.teek.security.utils.SecurityUtils;
import top.teek.utils.AddressUtil;
import top.teek.utils.MapstructUtil;
import top.teek.web.utils.ServletUtil;
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
 * @date 2024/6/22 16:16:01
 * @note
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    private final JwtProperties jwtProperties;
    private final LoginEventListen loginEventListen;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final AuthenticationManager authenticationManager;

    /**
     * 登录
     */
    public LoginVO login(LoginUserDTO loginUserDTO) {
        LoginUserBO loginUserBO = MapstructUtil.convert(loginUserDTO, LoginUserBO.class);
        
        // 检查用户是否被锁定登录
        loginEventListen.checkLogin(loginUserBO);

        long timeout = jwtProperties.getExpireTime();

        JwtAuthenticationToken token = new JwtAuthenticationToken(null, null);
        HttpServletRequest request = ServletUtil.getRequest();
        try {
            token = SecurityUtils.login(request, loginUserDTO.getUsername(), loginUserDTO.getPassword(), authenticationManager, timeout);

            // 走了自定义认证，则 Spring Security 不会调用自定义的成功处理器，这里需要手动调用
            // 参考 org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter.doFilter()
            loginSuccessHandler.onAuthenticationSuccess(request, ServletUtil.getResponse(), token.getAuthentication());
        } catch (InternalAuthenticationServiceException exception) {
            // 走了自定义认证，则 Spring Security 不会调用自定义的失败处理器，这里需要手动调用
            log.error("An internal error occurred while trying to authenticate the user.", exception);
            loginFailureHandler.onAuthenticationFailure(request, ServletUtil.getResponse(), exception, loginUserBO);
        } catch (AuthenticationException exception) {
            loginFailureHandler.onAuthenticationFailure(request, ServletUtil.getResponse(), exception, loginUserBO);
        }

        // 判断是否登录过且没过期。如果用户在浏览器 A 登录后，在浏览器 B 登录时，直接返回浏览器 A 登录的 token
        LoginUser loginUser = AgHelper.getLoginUser(loginUserDTO.getUsername());

        LoginVO loginVO = new LoginVO();
        if (Objects.nonNull(loginUser)) {
            loginVO.setAccessToken(loginUser.getAccessToken());
            loginVO.setRefreshToken(loginUser.getRefreshToken());
            loginVO.setExpireIn(timeout);
        } else {
            loginVO.setAccessToken(token.getAccessToken());
            loginVO.setRefreshToken(token.getRefreshToken());
            loginVO.setExpireIn(timeout);
            // 用户基本信息存入 Redis
            cacheLoginUser(token, timeout, request);
        }
        return loginVO;
    }

    /**
     * 退出登录
     */
    public boolean logout() {
        LoginUser loginUser = AgHelper.getLoginUser();
        boolean logout = AgHelper.logout();
        if (logout) {
            LoginInfoEvent loginInfoEvent = LoginInfoEvent.builder()
                    .userId(loginUser.getUserId())
                    .username(loginUser.getUsername())
                    .loginTime(LocalDateTime.now())
                    .status(AuthConstant.LOGOUT)
                    .request(ServletUtil.getRequest())
                    .message("退出成功")
                    .build();
            // 发布退出成功事件
            SpringHelper.publishEvent(loginInfoEvent);
        }
        return logout;
    }

    /**
     * 用户基本信息存入 Redis
     *
     * @param token      认证信息令牌
     * @param timeout    超时时间
     * @param request    请求
     */
    private void cacheLoginUser(JwtAuthenticationToken token, Long timeout, HttpServletRequest request) {
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
                .setLoginTime(LocalDateTime.now());

        AgHelper.cacheUserInfo(loginUser, timeout);
    }
}
