package top.teek.notice.system.model.po;

import top.teek.mp.annotation.FieldValueFill;
import top.teek.mp.annotation.ValueStrategy;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 邮箱配置
 */
@TableName(value ="t_notice_mail_config")
@Data
public class NoticeMailConfig implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 配置 ID
     */
    @TableField(fill = FieldFill.INSERT)
    @FieldValueFill(ValueStrategy.SNOWFLAKE)
    private String configId;

    /**
     * 类别
     */
    private String category;

    /**
     * 邮箱域名
     */
    private String host;

    /**
     * 邮箱端口
     */
    private Integer port;

    /**
     * 邮箱协议
     */
    private String protocol;

    /**
     * 用户账号
     */
    private String username;
    
    /**
     * 邮箱密码
     */
    private String password;
    
    /**
     * 邮箱昵称
     */
    private String nickname;

    /**
     * 发送失败后重试次数，默认失败后只重发一次
     */
    private Integer retryCount;

    /**
     * 是否显示 "此邮件由系统自动发送，请不要回复"；0：不显示，1：显示
     */
    private Integer showTips;

    /**
     * 邮件发送限制
     */
    private Integer sendLimit;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 应用 ID
     */
    private String appId;

    @Serial
    private static final long serialVersionUID = 1L;
}