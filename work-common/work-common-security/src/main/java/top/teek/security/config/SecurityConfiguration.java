package top.teek.security.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Teeker
 * @date 2024/6/22 19:04:34
 * @note
 */
@AutoConfiguration
@ConditionalOnProperty(value = "security.enabled", havingValue = "true")
@ComponentScan("top.teek.security.domain")
public class SecurityConfiguration {
}
