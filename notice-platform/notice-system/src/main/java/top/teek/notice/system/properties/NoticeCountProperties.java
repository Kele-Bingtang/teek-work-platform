package top.teek.notice.system.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Teeker
 * @date 2024/8/19 00:36:48
 * @note 多账户
 */
@Data
@Component
@ConfigurationProperties(prefix = "notice")
public class NoticeCountProperties {
    private Map<String, MailProperties> mail;
}
