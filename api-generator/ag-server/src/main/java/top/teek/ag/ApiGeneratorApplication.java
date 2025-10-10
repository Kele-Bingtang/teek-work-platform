package top.teek.ag;

import top.teek.ag.core.helper.AgHelper;
import top.teek.datasource.annotation.EnableDynamicDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;

/**
 * @author Teeker
 * @date 2024/6/19 22:15:34
 * @note
 */
@SpringBootApplication(scanBasePackages = "top.teek.ag")
@EnableDynamicDataSource
public class ApiGeneratorApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ApiGeneratorApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        AgHelper.init();
        System.out.println("(♥◠‿◠)ﾉﾞ Work API Generator 平台启动成功   ლ(´ڡ`ლ)ﾞ");
    }

    /**
     * 接触 tomcat8.0 以上版本对 URL 的特殊字符的限制，否则 key[]=1&&key[]=2 等 URL 会被拦截报错
     */
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(connector -> connector.setProperty("relaxedQueryChars", "|{}[]"));
        return factory;
    }
}
