package top.teek.ag.system.model.po;

import top.teek.ag.system.model.vo.TeamVO;
import top.teek.mp.annotation.FieldValueFill;
import top.teek.mp.annotation.ValueStrategy;
import top.teek.mp.base.BaseDO;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Teeker
 * @date 2024-04-23 01:04:36
 * @note 团队
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_team")
@Data
@AutoMapper(target = TeamVO.class, reverseConvertGenerate = false)
public class Team extends BaseDO {

    /**
     * 团队 ID
     */
    @TableField(fill = FieldFill.INSERT)
    @FieldValueFill(ValueStrategy.SNOWFLAKE)
    private String teamId;

    /**
     * 团队名字
     */
    private String teamName;

    /**
     * 团队介绍
     */
    private String description;

    /**
     * 负责人 ID
     */
    private String ownerId;

    /**
     * 负责人
     */
    private String ownerName;

    /**
     * 显示顺序
     */
    private Integer orderNum;
}