package top.teek.ratelimiter.config;

import top.teek.ratelimiter.aspect.RateLimitAspect;
import top.teek.ratelimiter.properties.RateLimitProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Teeker
 * @date 2024/6/11 00:25:34
 * @note
 */

@AutoConfiguration(after = RedisConfiguration.class)
@EnableConfigurationProperties(RateLimitProperties.class)
public class RateLimitConfiguration {

    @Bean
    public RateLimitAspect rateLimiterAspect(StringRedisTemplate stringRedisTemplate, RateLimitProperties rateLimitProperties) {
        return new RateLimitAspect(stringRedisTemplate, rateLimitProperties);
    }
}
