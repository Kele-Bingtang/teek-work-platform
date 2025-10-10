package top.teek.ag.system.model.dto;

import top.teek.ag.system.model.po.Project;
import top.teek.core.validate.RestGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/6/24 22:39:58
 * @note
 */
@Data
@AutoMapper(target = Project.class, reverseConvertGenerate = false)
public class ProjectDTO {
    /**
     * 主键
     */
    @NotNull(message = "id 不能为空", groups = {RestGroup.EditGroup.class})
    private Integer id;

    /**
     * 项目 ID
     */
    private String projectId;

    /**
     * 项目名
     */
    @NotBlank(message = "项目名不能为空", groups = {RestGroup.AddGroup.class, RestGroup.EditGroup.class})
    private String projectName;

    /**
     * 接口基础路径
     */
    @NotBlank(message = "基础接口路径不能为空", groups = {RestGroup.AddGroup.class, RestGroup.EditGroup.class})
    private String baseUrl;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 数据源 ID
     */
    private List<String> dataSourceId;

    /**
     * 0 项目创建者 1 项目加入者
     */
    @NotNull(message = "项目所属类型不能为空", groups = {RestGroup.QueryGroup.class})
    private Integer belongType;

    /**
     * 状态（0 禁用 1 启用）
     */
    private Integer status;

    /**
     * 团队 ID
     */
    @NotBlank(message = "团队 ID 不能为空", groups = {RestGroup.QueryGroup.class, RestGroup.AddGroup.class, RestGroup.DeleteGroup.class})
    private String teamId;
}
