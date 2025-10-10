package top.teek.tenant.config;

import top.teek.mp.config.MyBatisPlusAutoConfiguration;
import top.teek.redis.config.RedisTemplateConfig;
import top.teek.tenant.handler.PlusTenantLineHandler;
import top.teek.tenant.properties.TenantProperties;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Teeker
 * @date 2023/11/19 23:12
 * @note
 */
@EnableConfigurationProperties(TenantProperties.class)
@AutoConfiguration(after = {RedisTemplateConfig.class, MyBatisPlusAutoConfiguration.class})
@ConditionalOnProperty(value = "tenant.enable", havingValue = "true")
public class TenantAutoConfiguration {
    /**
     * 初始化租户配置
     */
    @Bean
    public boolean tenantInit(MybatisPlusInterceptor mybatisPlusInterceptor, TenantProperties tenantProperties) {
        List<InnerInterceptor> interceptors = new ArrayList<>();
        // 多租户插件 必须放到第一位
        interceptors.add(tenantLineInnerInterceptor(tenantProperties));
        interceptors.addAll(mybatisPlusInterceptor.getInterceptors());
        mybatisPlusInterceptor.setInterceptors(interceptors);
        return true;
    }

    /**
     * 多租户插件
     */
    public TenantLineInnerInterceptor tenantLineInnerInterceptor(TenantProperties tenantProperties) {
        return new TenantLineInnerInterceptor(new PlusTenantLineHandler(tenantProperties));
    }

}
