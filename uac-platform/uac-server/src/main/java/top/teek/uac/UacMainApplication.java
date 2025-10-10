package top.teek.uac;

import top.teek.uac.core.helper.UacHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;

/**
 * @author Teeker
 * @date 2023/11/12 2:35
 * @note
 */
@SpringBootApplication(scanBasePackages = "top.teek.uac")
public class UacMainApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(UacMainApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        UacHelper.init();
        System.out.println("(♥◠‿◠)ﾉﾞ Work UAC 平台启动成功   ლ(´ڡ`ლ)ﾞ");
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
