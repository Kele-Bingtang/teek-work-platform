package top.teek.core.http;

import top.teek.core.base.BaseCommonEnum;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

/**
 * @author Teeker
 * @date 2022/4/30 15:19
 * @note 响应状态枚举类
 */
@Getter
public enum ResponseStatusEnum implements BaseCommonEnum {
    /**
     * 规范响应体中的响应码和响应信息
     */
    SUCCESS(HttpServletResponse.SC_OK, "success", "操作成功"),
    /**
     * 操作失败
     */
    FAIL(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "fail", "操作失败"),
    /**
     * 操作错误
     */
    ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "操作错误"),
    /**
     * 请求未授权
     */
    UN_AUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "fail", "请求未授权"),

    /**
     * 客户端请求未授权
     */
    CLIENT_UN_AUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "fail", "客户端请求未授权"),

    /**
     * 404 没找到请求
     */
    NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, "fail", "404 没找到请求"),

    /**
     * 消息不能读取
     */
    MSG_NOT_READABLE(HttpServletResponse.SC_BAD_REQUEST, "fail", "消息不能读取"),

    /**
     * 不支持当前请求方法
     */
    METHOD_NOT_SUPPORTED(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "fail", "不支持当前请求方法"),

    /**
     * 不支持当前媒体类型
     */
    MEDIA_TYPE_NOT_SUPPORTED(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "fail", "不支持当前媒体类型"),

    /**
     * 请求被拒绝
     */
    REQ_REJECT(HttpServletResponse.SC_FORBIDDEN, "fail", "请求被拒绝"),

    /**
     * 服务器异常
     */
    INTERNAL_SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "fail", "服务器异常"),

    /**
     * 非法请求
     */
    ILLEGALITY_REQUEST(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "fail", "非法请求"),

    /**
     * 缺少必要的请求参数
     */
    PARAM_MISS(HttpServletResponse.SC_BAD_REQUEST, "fail", "缺少必要的请求参数"),

    /**
     * 请求参数类型错误
     */
    PARAM_TYPE_ERROR(HttpServletResponse.SC_BAD_REQUEST, "fail", "请求参数类型错误"),

    /**
     * 请求参数绑定错误
     */
    PARAM_BIND_ERROR(HttpServletResponse.SC_BAD_REQUEST, "fail", "请求参数绑定错误"),

    /**
     * 参数校验失败
     */
    PARAM_VALID_ERROR(HttpServletResponse.SC_BAD_REQUEST, "fail", "参数校验失败"),
    
    ;

    private Integer code;

    private String status;

    private String message;

    ResponseStatusEnum(Integer code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
