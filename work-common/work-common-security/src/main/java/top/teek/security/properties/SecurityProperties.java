package top.teek.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Teeker
 * @date 2024/4/11 上午12:54
 * @note
 */
@Data
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    /**
     * 是否禁用 Spring Security
     */
    private boolean enabled;
    /**
     * 排除路径
     */
    private String[] excludes;
}
