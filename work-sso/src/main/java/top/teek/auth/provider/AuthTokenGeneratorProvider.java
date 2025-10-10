package top.teek.auth.provider;

import top.teek.auth.vo.OpenApiTokenVO;
import top.teek.auth.properties.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Teeker
 * @date 2023/9/24 2:44
 * @note
 */

@Component
@Slf4j
@EnableConfigurationProperties(JwtProperties.class)
public class AuthTokenGeneratorProvider {
    private final JwtProperties jwtProperties;

    public AuthTokenGeneratorProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    } 
    
    // TODO 完成 Token 相关认证 
    public OpenApiTokenVO createTwoToken(String appId) {
        // 让 JWT 生产 Token 
        String accessToken = "";
        String refreshToken = "";
        // 将访问令牌和刷新令牌存储到数据库
        // 返回结果 
        return OpenApiTokenVO.builder().accessToken(accessToken).refreshToken(refreshToken).tokenType("Bearer").expireIn(jwtProperties.getOpenApiExpire()).build();
    }

    public OpenApiTokenVO refreshToken(String refreshToken, String appId) {
        return null;
    }
}