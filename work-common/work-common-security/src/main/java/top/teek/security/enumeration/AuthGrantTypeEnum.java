package top.teek.security.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Teeker
 * @date 2023/9/24 2:50
 * @note
 */
@AllArgsConstructor
@Getter
public enum AuthGrantTypeEnum {
    PASSWORD("password"), // 密码模式
    AUTHORIZATION_CODE("authorization_code"), // 授权码模式
    IMPLICIT("implicit"), // 简化模式
    CLIENT_CREDENTIALS("client_credentials"), // 客户端模式
    REFRESH_TOKEN("refresh_token"), // 刷新模式 
    ;

    private final String grantType;

    public static AuthGrantTypeEnum getByGrantType(String grantType) {
        for (AuthGrantTypeEnum value : values()) {
            if (value.getGrantType().equals(grantType)) {
                return value;
            }
        }
        return null;
    }
}