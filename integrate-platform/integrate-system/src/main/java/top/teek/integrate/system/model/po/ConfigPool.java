package top.teek.integrate.system.model.po;

import top.teek.integrate.system.model.bo.ConfigPoolBO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * API 配置池
 */
@TableName(value ="integrate_config_pool")
@Data
@AutoMapper(target = ConfigPoolBO.class, reverseConvertGenerate = false)
public class ConfigPool implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 唯一标识符
     */
    private String appId;

    /**
     * 来源系统接口 API 地址
     */
    private String apiUrl;

    /**
     * 调用系统请求方法：GET、POST 等
     */
    private String method;

    /**
     * 传给系统的参数
格式：[{"name": "", "type": "normal", "value": ""}, {"name": "", "type": "dynamic", "value": ""}]
- type 有 normal 和 dynamic 两种
- normal：value 是字符串
- dynamic：value 是时间动态参数，如 -2 就是基于当前时间减 2 天，+2 或 2 基于当前时间加 2天
     */
    private String reqParams;

    /**
     * 系统认证参数
     */
    private String authParams;

    /**
     * 系统认证地址
     */
    private String authUrl;

    /**
     * 截取系统读取的数据
     */
    private String interceptField;

    /**
     * 发送消息人
     */
    private String mailTo;

    /**
     * 消息发送类型：EM 邮件、SM 短信、OA WeLink
     */
    private String mailType;

    /**
     * 系统状态：1 启用，0 禁用
     */
    private Integer status;

    /**
     * 系统查询范围参数，格式：xx - xxx，其中 xx 是开始时间，xxx 是结束时间
     */
    private String rangeParams;

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