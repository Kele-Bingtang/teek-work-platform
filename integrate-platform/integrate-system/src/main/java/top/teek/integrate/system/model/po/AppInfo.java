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
 * 系统信息
 */
@TableName(value ="integrate_app_info")
@Data
public class AppInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 系统标识
     */
    private String appId;

    /**
     * 系统名字
     */
    private String appName;

    /**
     * 系统根路径
     */
    private String rootDomain;

    /**
     * 系统认证地址
     */
    private String authUrl;

    /**
     * 系统认证参数
     */
    private String authParams;

    /**
     * 系统状态：0 禁用，1 启用
     */
    private Integer status;

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