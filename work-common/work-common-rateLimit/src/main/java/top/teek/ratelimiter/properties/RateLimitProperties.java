package top.teek.ratelimiter.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Teeker
 * @date 2023/6/30 23:01
 * @note
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "redis-limit")
public class RateLimitProperties {
    
    /**
     * 访问次数
     */
    private Long limit;

    /**
     * 有效期,单位秒
     */
    private Long expire;

    /**
     * 限流 urls
     */
    private String[] urls;

}
