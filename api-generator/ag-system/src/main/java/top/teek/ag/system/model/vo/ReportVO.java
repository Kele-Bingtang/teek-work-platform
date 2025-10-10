package top.teek.ag.system.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Teeker
 * @date 2024/6/25 22:28:56
 * @note
 */
@Data
public class ReportVO implements Serializable {
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 报表 ID
     */
    private String reportId;

    /**
     * 报表名称
     */
    private String reportTitle;

    /**
     * 报名描述
     */
    private String description;

    /**
     * 是否允许新增（0 不允许，1 允许）
     */
    private Integer allowAdd;

    /**
     * 是否允许编辑（0 不允许，1 允许）
     */
    private Integer allowEdit;

    /**
     * 是否允许删除（0 不允许，1 允许）
     */
    private Integer allowRemove;

    /**
     * 是否允许查询（0 不允许，1 允许）
     */
    private Integer allowFilter;

    /**
     * 是否允许导出（0 不允许，1 允许）
     */
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
    private Integer pageSize;

    /**
     * 是否开启图标，0 不开启，1 饼图，2 折线图
     */
    private Integer chartType;

    /**
     * 目录 ID
     */
    private String categoryId;

    /**
     * 服务 ID
     */
    private String serviceId;

    /**
     * 项目 ID
     */
    private String projectId;

    /**
     * 团队 ID
     */
    private String teamId;

    @Serial
    private static final long serialVersionUID = 1L;
}
