package top.teek.ag.system.model.po;

import top.teek.ag.system.model.vo.ProjectVO;
import top.teek.mp.annotation.FieldValueFill;
import top.teek.mp.annotation.ValueStrategy;
import top.teek.mp.base.BaseDO;
import top.teek.mp.type.ListStringTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Teeker
 * @date 2024-03-23 01:03:43
 * @note 项目
*/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "t_project", autoResultMap = true)
@AutoMapper(target = ProjectVO.class, reverseConvertGenerate = false)
public class Project extends BaseDO {
    /**
     * 项目 ID
     */
    @TableField(fill = FieldFill.INSERT)
    @FieldValueFill(ValueStrategy.SNOWFLAKE)
    private String projectId;

    /**
     * 项目名
     */
    private String projectName;

    /**
     * 接口基础路径
     */
    private String baseUrl;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 项目密钥，唯一
     */
    private String secretKey;
    
    /**
     * 数据源 ID
     */
    @TableField(typeHandler = ListStringTypeHandler.class)
    private List<String> dataSourceId;

    /**
     * 团队 ID
     */
    private String teamId;
}