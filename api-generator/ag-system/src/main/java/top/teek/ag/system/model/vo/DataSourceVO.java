package top.teek.ag.system.model.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Teeker
 * @date 2024/6/30 21:02:06
 * @note
 */
@Data
public class DataSourceVO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 数据源 ID
     */
    private String dataSourceId;

    /**
     * 数据源名称
     */
    private String dataSourceName;

    /**
     * 数据源类型
     */
    private String dataSourceType;

    /**
     * 数据源驱动类
     */
    private String driverClassName;

    /**
     * 数据源链接地址
     */
    private String jdbcUrl;

    /**
     * 数据源服务器地址
     */
    private String host;

    /**
     * 数据源端口
     */
    private String port;

    /**
     * 数据源账号
     */
    private String username;

    /**
     * 数据源密码
     */
    private String password;

    /**
     * 数据源描述
     */
    private String description;

    /**
     * 密码加解密密钥
     */
    private String secretKey;

    /**
     * 状态（0 异常 1 正常）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 团队 ID
     */
    private String teamId;

    /**
     * 前端级联选择器回显的 dataSourceType 和 DriveClassName，get(0) 为 dataSourceType，get(1) 为 DriveClassName
     */
    private List<String> dataSourceTypeDriveClass;
}
