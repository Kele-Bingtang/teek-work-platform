package top.teek.encrypt.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Teeker
 * @date 2024/6/8 23:08:16
 * @note
 */
@Data
@ConfigurationProperties(prefix = "api-decrypt")
public class ApiDecryptProperties {
    /**
     * 加密开关
     */
    private Boolean enabled;

    /**
     * 头部标识，告诉系统是否解密传过来的数据
     */
    private String headerFlag;

    /**
     * 响应加密公钥
     */
    private String publicKey;

    /**
     * 请求解密私钥
     */
    private String privateKey;
}
