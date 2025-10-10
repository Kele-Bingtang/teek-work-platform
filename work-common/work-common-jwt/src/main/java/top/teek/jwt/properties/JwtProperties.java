package top.teek.jwt.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Teeker
 * @date 2023/11/6 23:01
 * @note
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * Token 密钥，自定义，根据密钥生成 token，或还原 token
     */
    private String secretKey = "work-platform-abcdefghijklmnopqrstuvwxyz-kbt";

    /**
     * Token 有效期
     */
    private long expireTime = 12 * 60 * 60 * 1000;

    /**
     * Token 刷新时间
     */
    private long refreshTime = 2 * 60 * 60 * 1000;
    
    private String authoritiesKey = "authorities";
}
