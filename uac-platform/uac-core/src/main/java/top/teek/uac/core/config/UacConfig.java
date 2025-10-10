package top.teek.uac.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Teeker
 * @date 2023/11/19 18:19
 * @note
 */
@Data
@Component
@ConfigurationProperties(prefix = "uac")
public class UacConfig {
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
