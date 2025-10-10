package top.teek.ag.system.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2024/6/25 22:17:14
 * @note
 */
@Data
public class ServiceInfoVO implements Serializable {
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 服务 ID
     */
    private String serviceId;

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 服务地址
     */
    private String serviceUrl;

    /**
     * 服务完整地址
     */
    private String fullUrl;

    /**
     * 服务描述
     */
    private String description;

    /**
     * 数据源 ID
     */
    private String dataSourceId;

    /**
     * 接口的查询 SQL 语句
     */
    private String selectSql;

    /**
     * 执行查询语句的表名
     */
    private String selectTable;

    /**
     * 执行插入语句的表名
     */
    private String insertTable;

    /**
     * 执行更新语句的表名
     */
    private String updateTable;

    /**
     * 执行删除语句的表名
     */
    private String deleteTable;

    /**
     * 是否进行认证（0 不认证 1 认证）
     */
    private Integer isAuth;

    /**
     * 报表 ID
     */
    private String reportId;

    /**
     * 报表标题
     */
    private String reportTitle;

    /**
     * 接口版本号（修改一次 +1）
     */
    private Integer serviceVersion;
    
    /**
     * 降级响应
     */
    private String breakingRespond;

    /**
     * 响应模板
     */
    private String responseTemplate;

    /**
     * 服务状态，0 禁用 1 启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 目录 ID
     */
    private String categoryId;

    /**
     * 项目 ID
     */
    private String projectId;

    /**
     * 团队 ID
     */
    private String teamId;

    /**
     * 是否存在列配置项
     */
    private boolean exitCol;

    /**
     * 是否禁用，作为下拉值用到
     */
    private boolean disabled;

    @Serial
    private static final long serialVersionUID = 1L;
}
