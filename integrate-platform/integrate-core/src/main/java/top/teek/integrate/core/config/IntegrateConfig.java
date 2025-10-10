package top.teek.integrate.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Teeker
 * @date 2024/10/29 23:21:07
 * @note
 */
@Data
@Component
@ConfigurationProperties(prefix = "integrate")
public class IntegrateConfig {
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
