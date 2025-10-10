package top.teek.security.utils;

import cn.hutool.core.codec.Base64Decoder;
import top.teek.helper.SpringHelper;
import top.teek.jwt.properties.JwtProperties;
import top.teek.security.JwtAuthenticationToken;
import top.teek.utils.IdsUtil;
import top.teek.utils.StringUtil;
import top.teek.web.utils.ServletUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.crypto.SecretKey;
import java.util.*;

/**
 * @author Teeker
 * @date 2022/12/10 21:37
 * @note JWT 工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtTokenUtils {

    /**
     * 生成令牌
     *
     * @param authentication 认证信息
     * @return 令牌
     */
    public static String generateToken(Authentication authentication) {
        JwtProperties jwtProperties = SpringHelper.getBean(JwtProperties.class);
        return generateToken(authentication, jwtProperties.getExpireTime(), null);
    }

    /**
     * 生成令牌
     *
     * @param authentication 认证信息
     * @param expireTime     Token 有效期
     * @return 令牌
     */
    public static String generateToken(Authentication authentication, Long expireTime) {
        return generateToken(authentication, expireTime, null);
    }

    public static String generateToken(Authentication authentication, Long expireTime, String namespace) {
        JwtProperties jwtProperties = SpringHelper.getBean(JwtProperties.class);
        Map<String, Object> claims = new HashMap<>(2);
        
        claims.put(jwtProperties.getAuthoritiesKey(), authentication.getAuthorities());
        claims.put("expireTime", expireTime);
        if (StringUtil.hasText(namespace)) {
            claims.put("namespace", namespace);
        }
        return generateToken(claims, expireTime);
    }

    /**
     * 生成令牌
     */
    private static String generateToken(Map<String, Object> claims, Long expireTime) {
        Date expirationDate = new Date(System.currentTimeMillis() + expireTime);
        String tenantId = SecurityUtils.getTenantId();
        String username = SecurityUtils.getUsername();
        return Jwts.builder()
                .header()
                .add("type", "JWT")
                .add("alg", "HS256")
                .and()
                .claims(claims)
                .id(IdsUtil.simpleUUID())
                .expiration(expirationDate)
                .subject(Objects.isNull(tenantId) ? username : tenantId + ":" + username)
                .issuer("work-uac")
                .issuedAt(new Date())
                .signWith(generateKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * 加密明文密钥
     */
    public static SecretKey generateKey() {
        JwtProperties jwtProperties = SpringHelper.getBean(JwtProperties.class);
        byte[] decodeKey = Base64Decoder.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(decodeKey);
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 根据请求令牌获取登录认证信息
     *
     * @param request 客户端的请求
     * @return 用户名
     */
    public static Authentication getAuthenticationFromRequest(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        return getAuthenticationFromToken(token);
    }

    /**
     * 根据请求令牌获取登录认证信息
     *
     * @param token jwt 密钥
     * @return 用户名
     */
    public static Authentication getAuthenticationFromToken(String token) {
        if (null == token) {
            return null;
        }
        Authentication authentication = null;
        // 如果在 Security 上下文检测没有登录过：1、程序重启后 session 清空。2、Spring Security 配置禁用 session，因此无法从 SecurityUtils.getAuthentication() 获取信息
        if (null == SecurityUtils.getAuthentication()) {
            // 根据 token 获取曾经登录的数据证明（防止恶意模拟 token）
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                return null;
            }
            String subject = claims.getSubject();
            if (subject == null) {
                return null;
            }
            // 如果 token 过期
            if (isTokenExpired(token)) {
                return null;
            }
            // 获取用户的权限列表
            JwtProperties jwtProperties = SpringHelper.getBean(JwtProperties.class);
            Object authors = claims.get(jwtProperties.getAuthoritiesKey());
            List<GrantedAuthority> authorities = new ArrayList<>();
            // 如果用户权限是集合，则存入新的集合里
            if (authors instanceof List authorList) {
                for (Object object : authorList) {
                    authorities.add(new SimpleGrantedAuthority((String) ((Map) object).get("authority")));
                }
            }
            authentication = new JwtAuthenticationToken(subject, new WebAuthenticationDetailsSource().buildDetails(ServletUtil.getRequest()), authorities, token, null);
        } else {
            // 如果上下文有用户登录过，则检查是否是当前用户
            if (validateToken(token, SecurityUtils.getUsername())) {
                // 如果上下文中 Authentication 非空，且请求令牌合法，直接返回当前登录认证信息
                authentication = SecurityUtils.getAuthentication();
            }
        }
        return authentication;
    }

    /**
     * 解析 Token
     *
     * @param token 令牌
     * @return 数据声明
     */
    private static Jws<Claims> parseClaimFromToken(String token) {
        return Jwts.parser().verifyWith(generateKey()).build().parseSignedClaims(token);
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private static Claims getClaimsFromToken(String token) {
        return parseClaimFromToken(token).getPayload();
    }

    /**
     * 从令牌中获取数据头
     *
     * @param token 令牌
     * @return 数据声明
     */
    private static JwsHeader getHeaderFromToken(String token) {
        return parseClaimFromToken(token).getHeader();
    }

    /**
     * 从令牌中获取 Subject（用户名）
     *
     * @param token 令牌
     * @return Subject
     */
    public static String getSubjectFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * 验证令牌
     *
     * @param token    令牌
     * @param username 用户名
     * @return 令牌是否正确
     */
    public static boolean validateToken(String token, String username) {
        String userName = getUsernameFromToken(token);
        return (username.equals(userName) && !isTokenExpired(token));
    }

    /**
     * 刷新令牌
     *
     * @param token 令牌
     * @return 新的令牌
     */
    public static String refreshToken(String token) {
        JwtProperties jwtProperties = SpringHelper.getBean(JwtProperties.class);
        return refreshToken(token, jwtProperties.getExpireTime());
    }

    public static String refreshToken(String token, Long expireTime) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            refreshedToken = generateToken(claims, expireTime);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取 Token 有效期
     *
     * @param token 令牌
     * @return Token 有效期
     */
    public static Date getTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getExpiration();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取 Token 有效期时间，如 720000 为 2 小时
     *
     * @param token 令牌
     * @return Token 有效期
     */
    public static Long getTokenExpireTime(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Integer expireTime = (Integer) claims.get("expireTime");
            return Long.valueOf(expireTime);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据请求获取 token
     *
     * @param request 用户请求
     * @return 令牌
     */
    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        String tokenHead = "Bearer ";
        if (Objects.isNull(token)) {
            token = request.getHeader("token");
            if (Objects.isNull(token)) {
                token = request.getParameter("token");
            }
        } else if (token.contains(tokenHead)) {
            // 把 Bearer 去掉，只要后面的 token 值
            token = token.substring(tokenHead.length());
        }
        if ("".equals(token)) {
            token = null;
        }
        return token;
    }

    public static Long getTokenTimeout() {
        JwtProperties jwtProperties = SpringHelper.getBean(JwtProperties.class);
        return jwtProperties.getExpireTime();
    }

}
