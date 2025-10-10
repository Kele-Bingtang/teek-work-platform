package top.teek.doc.properties;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Teeker
 * @date 2023/7/6 22:24
 * @note
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("springdoc.info")
public class SpringDocProperties {

    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 作者
     */
    private String author;
    /**
     * 版本
     */
    private String version;
    /**
     * url
     */
    private String url;
    /**
     * email
     */
    private String email;

    /**
     * license
     */
    private License license;

    /**
     * license-url
     */
    private String licenseUrl;

    /**
     * license-url
     */
    private Contact contact;

}