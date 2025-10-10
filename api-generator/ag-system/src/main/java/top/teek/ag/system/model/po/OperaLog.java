package top.teek.ag.system.model.po;

import top.teek.mp.annotation.FieldValueFill;
import top.teek.mp.annotation.ValueStrategy;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Teeker
 * @date 2024-03-23 01:03:37
 * @note 操作日志记录
*/
@TableName(value ="t_opera_log")
@Data
public class OperaLog implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 日志 ID
     */
    @TableField(fill = FieldFill.INSERT)
    @FieldValueFill(ValueStrategy.SNOWFLAKE)
    private String operaId;

    /**
     * 模块标题
     */
    private String title;

    /**
     * 业务类型（0 其它 1 新增 2 修改 3 删除）
     */
    private Integer businessType;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 操作类别（0 其它 1 后台用户 2 手机端用户）
     */
    private Integer operatorType;

    /**
     * 操作人员
     */
    private String operaName;

    /**
     * 请求 URL
     */
    private String operaUrl;

    /**
     * 主机地址
     */
    private String operaIp;

    /**
     * 操作地点
     */
    private String operaLocation;

    /**
     * 请求参数
     */
    private String operaParam;

    /**
     * 返回参数
     */
    private String jsonResult;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 状态（0 异常 1 正常 ）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date operaTime;

    /**
     * 消耗时间
     */
    private Integer costTime;

    @Serial
    private static final long serialVersionUID = 1L;
}