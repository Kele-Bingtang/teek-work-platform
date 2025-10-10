package top.teek.notice.system.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 短信、邮件发送记录
 */
@TableName(value ="t_notice_info")
@Data
public class NoticeInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 消息 ID
     */
    private String noticeId;

    /**
     * 应用 ID
     */
    private String appId;

    /**
     * 消息类型（SM 短信 EM 邮件）
     */
    private String messageType;

    /**
     * 发件人
     */
    @TableField(value = "`from`")
    private String from;

    /**
     * 发件人别称
     */
    private String fromName;

    /**
     * 收件人
     */
    @TableField(value = "`to`")
    private String to;

    /**
     * 抄送人
     */
    private String cc;

    /**
     * 主题
     */
    private String subject;

    /**
     * 内容
     */
    private String content;

    /**
     * 
     */
    private Integer priority;

    /**
     * 类别
     */
    private String category;

    /**
     * 短息模板 ID
     */
    private String smsTemplateId;

    /**
     * 短信 ID
     */
    private String smsMessageId;

    /**
     * New 未发送，Ready 准备发送，Processing 发送中，Completed 发送成功，Failed：发送失败，Hold 挂起、暂停发送
     */
    private String status;

    /**
     * 是否需要重试（N 否 Y 是）。default 为 N，因收件人地址错误或是号码错误导致发送失败，不会重试
     */
    private String retry;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 文件 ID,多个用英文逗号隔开
     */
    private String fileIds;

    /**
     * 业务 ID
     */
    private String bizId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除（0 否 1 是）
     */
    private Integer isDeleted;

    @Serial
    private static final long serialVersionUID = 1L;
}