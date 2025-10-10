package top.teek.integrate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Teeker
 * @date 2024/10/28 00:17:08
 * @note
 */
@SpringBootApplication(scanBasePackages = "top.teek.integrate", exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@EnableFeignClients(basePackages = "top.teek.integrate")
public class IntegrateApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(IntegrateApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ Work 集成平台启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
