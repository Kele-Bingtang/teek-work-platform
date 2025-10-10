package top.teek.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * @author Teeker
 * @date 2023/9/24 3:07
 * @note
 */
@SpringBootApplication(scanBasePackages = "top.teek.file", exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
public class FileStoreApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FileStoreApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ Work 文件存储仓库平台启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
