package top.teek.web.config;

import top.teek.web.annotation.resolver.RequestUriArgumentResolver;
import top.teek.web.annotation.resolver.RequestUrlArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Teeker
 * @date 2024/11/6 21:04:42
 * @note
 */
@Configuration
public class WebMvcConfiguration  implements WebMvcConfigurer {

    /**
     * 解决跨域问题
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true)
                .allowedOriginPatterns("*")
                .maxAge(3600);
    }

    /**
     * 增加 GET 请求参数中时间类型转换
     * <ul>
     * <li> HH:mm:ss -> LocalTime </li>
     * <li> yyyy-MM-dd -> LocalDate </li>
     * <li> yyyy-MM-dd HH:mm:ss -> LocalDateTime </li>
     * </ul>
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setTimeFormatter(DateTimeFormatter.ofPattern("HH:mm:ss"));
        registrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        registrar.registerFormatters(registry);
    }

    /**
     * 添加自定义注解
     *
     * @param resolvers initially an empty list
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RequestUrlArgumentResolver());
        resolvers.add(new RequestUriArgumentResolver());
    }
}