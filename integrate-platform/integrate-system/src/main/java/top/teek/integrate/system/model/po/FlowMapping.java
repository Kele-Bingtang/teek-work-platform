package top.teek.integrate.system.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统映射信息
 */
@TableName(value ="integrate_flow_mapping")
@Data
public class FlowMapping implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标识符
     */
    private String appId;

    /**
     * 来源系统 ID
     */
    private Long sourceId;

    /**
     * 目标系统 ID
     */
    private Long targetId;

    /**
     * 第三方系统 ID
     */
    private String extraId;

    /**
     * 系统状态：0 禁用，1 启用
     */
    private Integer status;

    /**
     * 发送人
     */
    private String mailTo;

    /**
     * 消息发送类型：EM 邮件、SM 短信、OA WeLink
     */
    private String mailType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新用户
     */
    private String updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}