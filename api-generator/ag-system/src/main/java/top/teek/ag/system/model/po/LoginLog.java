package top.teek.ag.system.model.po;

import top.teek.mp.annotation.FieldValueFill;
import top.teek.mp.annotation.ValueStrategy;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2024-03-23 01:03:32
 * @note 操作日志记录
*/
@TableName(value ="t_login_log")
@Data
@Builder
public class LoginLog implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 登录 ID
     */
    @TableField(fill = FieldFill.INSERT)
    @FieldValueFill(ValueStrategy.SNOWFLAKE)
    private String loginId;

    /**
     * 用户账号
     */
    private String username;

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
     * 操作系统
     */
    private String os;

    /**
     * 提示消息
     */
    private String msg;

    /**
     * 状态（0 异常 1 正常 ）
     */
    private Integer status;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    @Serial
    private static final long serialVersionUID = 1L;
}