package top.teek.notice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * @author Teeker
 * @date 2024-08-05 01:05:06
 * @note
 */
@SpringBootApplication(scanBasePackages = "top.teek.notice", exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
public class NoticePlatformApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(NoticePlatformApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ Work 消息发送平台启动成功   ლ(´ڡ`ლ)ﾞ");
    }

}
