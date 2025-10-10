package top.teek.uac.core.bo;

import lombok.Data;

/**
 * @author Teeker
 * @date 2023/11/14 23:15
 * @note
 */
@Data
public class LoginUserBO {
    /**
     * 租户 ID
     */
    private String tenantId;
    /**
     * 应用 ID
     */
    private String appId;
    /**
     * 用户名
     */
    private String username;
    /**
     * "用户密码
     */
    private String password;
    /**
     * 授权类型
     */
    private String grantType;

    /**
     * 应用码
     */
    private String appCode;

    /**
     * 应用名
     */
    private String appName;
    
    /**
     * 客户端名
     */
    private String clientName;
}
