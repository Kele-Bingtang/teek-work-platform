package top.teek.file.system.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Teeker
 * @date 2024/8/5 21:57:44
 * @note
 */
@ConfigurationProperties(prefix = "file")
@Data
@Component
public class FileProperties {
    private String fileStorePath;
    private Integer expireTime = 3650; // 99 年
    private Integer maxFileSize = 500 * 1024; // 500MB
    private Integer maxFileNameSize = 300;
}
