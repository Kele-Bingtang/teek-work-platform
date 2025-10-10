package top.teek.cache.config;

import top.teek.cache.manager.CaffeineRedisCacheManager;
import top.teek.cache.properties.CaffeineCacheProperties;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * @author Teeker
 * @date 2024/6/11 22:44:26
 * @note 缓存配置
 */
@EnableCaching
@AutoConfiguration(after = RedisConnectionFactory.class)
@EnableConfigurationProperties({CaffeineCacheProperties.class, CacheProperties.class})
public class CacheConfiguration {
    /**
     * caffeine 本地缓存处理器
     */
    @Bean
    public Cache<Object, Object> caffeine(CaffeineCacheProperties properties) {
        return Caffeine.newBuilder()
                // 过期时间
                .expireAfterWrite(properties.getExpire())
                // 初始的缓存空间大小
                .initialCapacity(properties.getCapacity())
                // 缓存的最大条数
                .maximumSize(properties.getMaximumSize())
                .build();
    }

    /**
     * 自定义缓存管理器 整合spring-cache
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, CacheProperties properties) {
        return new CaffeineRedisCacheManager(redisConnectionFactory, properties);
    }
}
