package top.teek.uac.core.constant;

/**
 * @author Teeker
 * @date 2023/11/13 22:31
 * @note
 */
public interface AuthConstant {
    
    String LOGIN_SUCCESS = "Login_Success";
    String LOGIN_FAIL = "Login_Error";
    String LOGOUT = "Logout_Success";
    String REGISTER = "Register_Success";
    String TENANT_ID = "tenantId";
    
    String USERNAME = "username";
    String PASSWORD = "password";
    
    String CLIENT_NAME = "clientName";
    
    String JWT_TOKEN = "Jwt-Token";
    
    /**
     * 用户名长度限制
     */
    int USERNAME_MIN_LENGTH = 2;
    int USERNAME_MAX_LENGTH = 20;

    /**
     * 密码长度限制
     */
    int PASSWORD_MIN_LENGTH = 5;
    int PASSWORD_MAX_LENGTH = 20;
    
}
