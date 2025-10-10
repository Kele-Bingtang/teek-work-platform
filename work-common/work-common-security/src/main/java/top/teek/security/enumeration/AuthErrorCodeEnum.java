package top.teek.security.enumeration;

import top.teek.core.base.BaseCommonEnum;
import lombok.AllArgsConstructor;

/**
 * @author Teeker
 * @date 2023/9/24 2:49
 * @note
 */
@AllArgsConstructor
public enum AuthErrorCodeEnum implements BaseCommonEnum {
    BAD_REQUEST(400, "fail", "请求参数不正确"),
    FORBIDDEN(403, "fail", "没有该操作权限"),
    UN_AUTHORIZED(7000, "fail", "用户未登录"),
    AUTH_APP_NAME_IS_EXISTS(7001, "fail", "应用已存在，请确认应用信息"),
    AUTH_APP_CODE_IS_EXISTS(7002, "fail", "应用编码已存在，请修改后提交"),
    AUTH_APP_NOT_EXISTS(7003, "fail", "未认证应用"),
    AUTH_APP_SECRET_ERROR(7004, "fail", "无效 appSecret"),
    AUTH_APP_STATUS_ENABLE(7005, "fail", "应用已被停用"),
    AUTH_APP_GRANT_TYPE_NOT_SUPPORT(7006, "fail", "未授予该授权模式"),
    AUTH_APP_GRANT_TYPE_NOT_REALIZE(7007, "fail", "授权模式未实现"),
    AUTH_JWT_ERROR(7004, "fail", "无效 Jwt 密钥"),
    GRANT_TYPE_REFRESH_TOKEN_IS_EMPTY(8001, "fail", "请传入正确的 refresh_token 以进行操作"),
    LOGIN_FAIL(1003, "fail", "登录失败"),
    USER_ACCOUNT_EXPIRED(1006, "fail", "账号过期"),
    USERNAME_PASSWORD_ERROR(1007, "fail", "用户名或密码错误"),
    USER_PASSWORD_EXPIRED(1008, "fail", "账号过期"),
    USER_ACCOUNT_DISABLE(1009, "fail", "账号禁用"),
    USER_ACCOUNT_LOCKED(1010, "fail", "账号锁定"),
    USER_ACCOUNT_NOT_EXIST(1011, "fail", "账号不存在"),

    ;

    private Integer code;
    private String status;
    private String message;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}