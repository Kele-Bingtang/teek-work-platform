package top.teek.uac.system.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 操作日志记录
 *
 * @TableName t_sys_login_log
 */
@Data
public class SysLoginLogDTO implements Serializable {
    /**
     * 用户账号
     */
    private String username;

    /**
     * 客户端名
     */
    private String clientName;

    /**
     * 登录 IP 地址
     */
    private String loginIp;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 状态（0 异常 1 正常 ）
     */
    private Integer status;

    /**
     * 登录时间
     */
    private List<String> loginTime;

    @Serial
    private static final long serialVersionUID = 1L;
}