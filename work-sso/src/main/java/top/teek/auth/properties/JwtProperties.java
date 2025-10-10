package top.teek.auth.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Teeker
 * @date 2023/9/24 2:51
 * @note
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "token")
public class JwtProperties {
    /**
     * 密匙Key
     */
    public String secret;
    /**
     * HeaderKey
     */
    public String tokenHeader;
    /**
     * Token 前缀
     */
    public String tokenPrefix;
    /**
     * token 过期时间，单位秒
     */
    public Long expireIn;
    /**
     * 过期时间，单位秒
     */
    public Long expiration;
    /**
     * openApi token 过期时间，单位秒
     */
    private Long openApiExpire;
    /**
     * 重刷时间,单位秒
     */
    public Long refreshTime;
    /**
     * 配置白名单
     */
    public String antMatchers;
}