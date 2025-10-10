package top.teek.log.properties;

import top.teek.log.enumeration.RequestLogLevelEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Teeker
 * @date 2023/6/30 22:48
 * @note
 */
@Getter
@Setter
@ConfigurationProperties(value = RequestLogLevelEnum.REQ_LOG_PROPS_PREFIX)
public class RequestLogProperties {
    /**
     * 日志级别配置，默认：BODY
     */
    private RequestLogLevelEnum level = RequestLogLevelEnum.BODY;
}