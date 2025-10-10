package top.teek.ag.system.model.dto;

import top.teek.ag.system.model.po.DataSource;
import top.teek.core.validate.RestGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Teeker
 * @date 2024/6/30 21:03:52
 * @note
 */
@Data
@AutoMapper(target = DataSource.class, reverseConvertGenerate = false)
public class DataSourceDTO {
    /**
     * 主键
     */
    @NotNull(message = "id 不能为空", groups = {RestGroup.EditGroup.class, RestGroup.DeleteGroup.class})
    private Long id;
    /**
     * 数据源 ID
     */
    private String dataSourceId;

    /**
     * 数据源名称
     */
    @NotBlank(message = "数据源名称不能为空", groups = {RestGroup.AddGroup.class, RestGroup.EditGroup.class})
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
    @NotBlank(message = "数据源链接 URL不能为空", groups = {RestGroup.AddGroup.class, RestGroup.EditGroup.class})
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
     * 团队 ID
     */
    @NotBlank(message = "团队 ID 不能为空", groups = {RestGroup.QueryGroup.class, RestGroup.AddGroup.class, RestGroup.EditGroup.class, RestGroup.DeleteGroup.class})
    private String teamId;
}
