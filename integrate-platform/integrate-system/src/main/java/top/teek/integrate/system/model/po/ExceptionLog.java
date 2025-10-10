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
 * 异常日志
 */
@TableName(value ="integrate_exception_log")
@Data
public class ExceptionLog implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 配置信息的 id
     */
    private Long configId;

    /**
     * 异常信息
     */
    private String exceptionInfo;

    /**
     * 异常接口地址
     */
    private String exceptionUrl;

    /**
     * 返回信息
     */
    private String returnInfo;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}