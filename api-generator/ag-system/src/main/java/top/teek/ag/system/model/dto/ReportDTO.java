package top.teek.ag.system.model.dto;

import top.teek.ag.system.model.po.Report;
import top.teek.core.validate.RestGroup;
import top.teek.core.validate.annotation.IncludeValid;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Teeker
 * @date 2024/6/25 22:30:49
 * @note
 */
@Data
@AutoMapper(target = Report.class, reverseConvertGenerate = false)
@Accessors(chain = true)
public class ReportDTO {
    /**
     * 主键
     */
    @NotNull(message = "id 不能为空", groups = {RestGroup.EditGroup.class, RestGroup.DeleteGroup.class})
    private Long id;

    /**
     * 报表 ID
     */
    private String reportId;

    /**
     * 报表名称
     */
    @NotBlank(message = "报表名称不能为空", groups = {RestGroup.AddGroup.class, RestGroup.EditGroup.class})
    private String reportTitle;

    /**
     * 报名描述
     */
    private String description;

    /**
     * 是否允许新增（0 不允许，1 允许）
     */
    @IncludeValid(value = {"0", "1"}, message = "是否允许新增配置不能为空")
    private Integer allowAdd;

    /**
     * 是否允许编辑（0 不允许，1 允许）
     */
    @IncludeValid(value = {"0", "1"}, message = "是否允许编辑配置不能为空")
    private Integer allowEdit;

    /**
     * 是否允许删除（0 不允许，1 允许）
     */
    @IncludeValid(value = {"0", "1"}, message = "是否允许删除配置不能为空")
    private Integer allowRemove;

    /**
     * 是否允许查询（0 不允许，1 允许）
     */
    @IncludeValid(value = {"0", "1"}, message = "是否允许查询配置不能为空")
    private Integer allowFilter;

    /**
     * 是否允许导出（0 不允许，1 允许）
     */
    @IncludeValid(value = {"0", "1"}, message = "是否允许导出配置不能为空")
    private Integer allowExport;

    /**
     * 报表是否出现行数（0 不允许，1 允许）
     */
    private Integer allowRow;

    /**
     * 弹出框宽度
     */
    private String dialogWidth;

    /**
     * 一页显示多少条数据
     */
    @Min(value = 1, message = "不能低于 1 条")
    @Max(value = 999, message = "不能高于 999 条")
    private Integer pageSize;

    /**
     * 是否开启图标，0 不开启，1 饼图，2 折线图
     */
    @IncludeValid(value = {"0", "1", "2"}, message = "是否开启图标配置不能为空")
    private Integer chartType;

    /**
     * 目录 ID
     */
    @NotBlank(message = "目录 ID 不能为空", groups = {RestGroup.AddGroup.class})
    private String categoryId;

    /**
     * 服务 ID
     */
    @NotBlank(message = "服务 ID 不能为空", groups = {RestGroup.QueryGroup.class, RestGroup.AddGroup.class})
    private String serviceId;

    /**
     * 项目 ID
     */
    @NotBlank(message = "项目 ID 不能为空", groups = {RestGroup.AddGroup.class})
    private String projectId;

    /**
     * 团队 ID
     */
    @NotBlank(message = "团队 ID 不能为空", groups = {RestGroup.AddGroup.class})
    private String teamId;
}
