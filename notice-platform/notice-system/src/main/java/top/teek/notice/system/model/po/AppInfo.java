package top.teek.notice.system.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * app 配置表
 */
@TableName(value ="t_app_info")
@Data
public class AppInfo implements Serializable {
    /**
     * 主键 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 应用 ID
     */
    private String appId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 负责人 ID
     */
    private String owner;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否使用默认发送器（0 不使用 1 使用）
     */
    private Integer defaultSender;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建人 ID
     */
    private String createById;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新人 ID
     */
    private String updateById;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 状态（0 异常 1 正常 ）
     */
    private Integer status;

    /**
     * 是否删除（0 否 1 是）
     */
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDeleted;

    @Serial
    private static final long serialVersionUID = 1L;
}