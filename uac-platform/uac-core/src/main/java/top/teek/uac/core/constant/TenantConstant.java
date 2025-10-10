package top.teek.uac.core.constant;

/**
 * @author Teeker
 * @date 2024/4/20 22:59:09
 * @note
 */
public interface TenantConstant {

    /**
     * 超级管理员角色 roleCode
     */
    String SUPER_ADMIN_ROLE_KEY = "superadmin";

    /**
     * 租户管理员角色 roleCode
     */
    String TENANT_ADMIN_ROLE_KEY = "admin";

    /**
     * 租户管理员角色名称
     */
    String TENANT_ADMIN_ROLE_NAME = "管理员";

    /**
     * 默认租户ID
     */
    String DEFAULT_TENANT_ID = "000000";
    /**
     * 默认 APP ID
     */
    String DEFAULT_UAC_APP_ID = "1000000000000000000";
    /**
     * 默认初创的用户 ID
     */
    String DEFAULT_USER_ID = "1000000000000000000";
    /**
     * 默认超级管理员角色 ID
     */
    String DEFAULT_ROLE_ID = "1000000000000000000";
}
