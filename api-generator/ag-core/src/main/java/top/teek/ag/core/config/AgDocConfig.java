package top.teek.ag.core.config;

import top.teek.doc.config.SpringDocAutoConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Teeker
 * @date 2024/7/3 22:27:35
 * @note
 */
@Configuration(proxyBeanMethods = false)
public class AgDocConfig {
    /**
     * system 模块的 API 分组
     */
    @Bean
    public GroupedOpenApi agGroupedOpenApi() {
        return SpringDocAutoConfiguration.buildGroupedOpenApi("API Generator");
    }
}
