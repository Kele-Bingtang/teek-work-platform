package top.teek.security.utils;

import top.teek.security.JwtAuthenticationToken;
import top.teek.security.domain.SecurityUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.Objects;


/**
 * @author Teeker
 * @date 2022/12/10 21:42
 * @note Spring Security 认证工具
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

    /**
     * 系统登录认证
     *
     * @param request               客户端请求
     * @param username              当前用户名
     * @param password              当前用户密码
     * @param authenticationManager 认证对象
     * @return 认证后的用户信息，内容包括 token
     */
    public static JwtAuthenticationToken login(HttpServletRequest request, String username, String password, AuthenticationManager authenticationManager) {
        return login(request, username, password, authenticationManager, null);
    }

    /**
     * 系统登录认证
     *
     * @param request               客户端请求
     * @param username              当前用户名
     * @param password              当前用户密码
     * @param authenticationManager 认证对象
     * @param expireTime            Token 有效期
     * @return 认证后的用户信息，内容包括 token
     */
    public static JwtAuthenticationToken login(HttpServletRequest request, String username, String password, AuthenticationManager authenticationManager, Long expireTime) {
        JwtAuthenticationToken token = new JwtAuthenticationToken(username, password);
        // token 存入 request 的相关信息
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // 取代 org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.attemptAuthentication() 的 this.getAuthenticationManager().authenticate(authRequest) 方法
        // 执行登录认证过程，获取调用 UserDetailsServiceImpl 的 loadUserByUsername 方法
        Authentication authentication = authenticationManager.authenticate(token);
        // 认证成功存储认证信息到上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌并返回给客户端 
        token.setAccessToken(Objects.nonNull(expireTime) ? JwtTokenUtils.generateToken(authentication, expireTime) : JwtTokenUtils.generateToken(authentication));
        token.setAuthentication(authentication);
        return token;
    }

    /**
     * 获取令牌进行认证
     *
     * @param request 客户端请求
     */
    public static void checkAuthentication(HttpServletRequest request) {
        // 获取令牌并根据令牌获取登录认证信息
        Authentication authentication = JwtTokenUtils.getAuthenticationFromRequest(request);
        // 设置登录认证信息到上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 从上下文获取当前用户名
     *
     * @return 当前用户名
     */
    public static String getUsername() {
        return getUsername(getAuthentication());
    }

    /**
     * 从传入的认证信息中获取用户名
     *
     * @return 用户名
     */
    public static String getUsername(Authentication authentication) {
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails userdetails) {
                return userdetails.getUsername();
            }

            // JWT token 可能格式为 tenantId:username
            String userKey = String.valueOf(authentication.getPrincipal());

            if (userKey.contains(":")) {
                return userKey.split(":")[1];
            }
            return String.valueOf(authentication.getPrincipal());
        }
        return null;
    }

    /**
     * 从上下文获取当前登录信息
     *
     * @return 当前登录信息
     */
    public static Authentication getAuthentication() {
        if (SecurityContextHolder.getContext() == null) {
            return null;
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Object getPrincipal() {
        Authentication authentication = getAuthentication();
        if (Objects.nonNull(authentication)) {
            return authentication.getPrincipal();
        }
        return null;
    }

    public static String getTenantId() {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof SecurityUser securityUser) {
                return securityUser.getTenantId();
            }

            // JWT token 可能格式为 tenantId:username
            String userKey = String.valueOf(authentication.getPrincipal());
            
            if (userKey.contains(":")) {
                return userKey.split(":")[0];
            }
        }
        return null;
    }
}
