package top.teek.uac.core.event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2023/11/17 0:28
 * @note 登录信息事件类
 */
@Data
@Builder
public class LoginInfoEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 租户 ID
     */
    private String tenantId;

    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 客户端名
     */
    private String clientName;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 登录状态 0成功 1失败
     */
    private String status;

    /**
     * 提示消息
     */
    private String message;

    /**
     * 请求体
     */
    private HttpServletRequest request;

    /**
     * 其他参数
     */
    private Object[] args;
}