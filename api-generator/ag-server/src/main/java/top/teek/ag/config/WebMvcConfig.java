package top.teek.ag.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Teeker
 * @date 2024/6/19 22:41:37
 * @note
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 解决跨域问题
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(false)
                .allowedOriginPatterns("*")
                .maxAge(3600);
    }
}
