package top.teek.idempotent.config;

import top.teek.idempotent.aspect.PreventRepeatSubmitAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConfiguration;

/**
 * @author Teeker
 * @date 2024/6/9 16:41:36
 * @note 接口幂等功能配置
 */
@AutoConfiguration(after = RedisConfiguration.class)
public class IdempotenceConfiguration {
    @Bean
    public PreventRepeatSubmitAspect repeatSubmitAspect() {
        return new PreventRepeatSubmitAspect();
    }
}
