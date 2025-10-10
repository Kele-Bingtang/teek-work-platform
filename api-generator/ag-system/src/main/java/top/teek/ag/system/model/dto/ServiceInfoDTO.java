package top.teek.ag.system.model.dto;

import top.teek.ag.system.model.po.ServiceInfo;
import top.teek.core.validate.RestGroup;
import top.teek.core.validate.annotation.IncludeValid;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Teeker
 * @date 2024/6/25 22:21:59
 * @note
 */
@Data
@AutoMapper(target = ServiceInfo.class, reverseConvertGenerate = false)
public class ServiceInfoDTO {
    /**
     * 主键
     */
    @NotNull(message = "id 不能为空", groups = {RestGroup.EditGroup.class})
    private Long id;

    /**
     * 服务 ID
     */
    @NotBlank(message = "团队 ID 不能为空", groups = {RestGroup.OtherGroup.class})
    private String serviceId;

    /**
     * 服务名
     */
    @NotBlank(message = "服务名称不能为空", groups = {RestGroup.AddGroup.class})
    private String serviceName;

    /**
     * 服务地址
     */
    @NotEmpty(message = "接口地址不能为空", groups = {RestGroup.AddGroup.class})
    private String serviceUrl;

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
     * 报表名称
     */
    private String reportTitle;

    /**
     * 接口版本号（修改一次 +1）
     */
    private Integer serviceVersion;

    /**
     * 降级响应
     */
    @AutoMapping(ignore = true)
    private Object breakingRespond;

    /**
     * 响应模板
     */
    @AutoMapping(ignore = true)
    private Object responseTemplate;

    /**
     * 接口状态，0 禁用 1 启用
     */
    @IncludeValid(value = {"0", "1"}, message = "接口状态不能为空")
    private Integer status;

    /**
     * 目录 ID
     */
    @NotBlank(message = "目录 ID 不能为空", groups = {RestGroup.QueryGroup.class, RestGroup.AddGroup.class, RestGroup.OtherGroup.class})
    private String categoryId;

    /**
     * 项目 ID
     */
    @NotBlank(message = "项目 ID 不能为空", groups = {RestGroup.AddGroup.class, RestGroup.EditGroup.class, RestGroup.OtherGroup.class})
    private String projectId;

    /**
     * 团队 ID
     */
    @NotBlank(message = "团队 ID 不能为空", groups = {RestGroup.AddGroup.class, RestGroup.DeleteGroup.class, RestGroup.OtherGroup.class})
    private String teamId;
}
