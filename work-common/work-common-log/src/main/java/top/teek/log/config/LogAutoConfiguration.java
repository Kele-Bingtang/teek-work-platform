package top.teek.log.config;

import top.teek.log.properties.RequestLogProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author Teeker
 * @date 2023/6/30 22:50
 * @note
 */
@AutoConfiguration
@EnableConfigurationProperties({RequestLogProperties.class})
public class LogAutoConfiguration {

}