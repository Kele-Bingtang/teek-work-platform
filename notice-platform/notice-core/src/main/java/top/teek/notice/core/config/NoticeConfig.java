package top.teek.notice.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Teeker
 * @date 2024/8/5 01:02:45
 * @note
 */
@Data
@Component
@ConfigurationProperties(prefix = "notice")
public class NoticeConfig {
    /**
     * 项目名称
     */
    private String name;

    /**
     * 版本
     */
    private String version;

    /**
     * 版权年份
     */
    private String copyrightYear;
}
