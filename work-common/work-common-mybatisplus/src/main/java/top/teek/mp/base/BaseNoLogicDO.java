package top.teek.mp.base;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2024/6/28 21:55:06
 * @note 没有逻辑删除的 BaseDO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public abstract class BaseNoLogicDO implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * pk
     */
    @Schema(description = "pk")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    public Long id;

    /**
     * 创建者
     */
    @Schema(description = "创建人")
    @TableField(value = "create_by",fill = FieldFill.INSERT)
    public String createBy;

    /**
     * 创建者id
     */
    @Schema(description = "创建人id")
    @TableField(value = "create_by_id",fill = FieldFill.INSERT)
    public String createById;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    public LocalDateTime createTime;

    /**
     * 更新者
     */
    @Schema(description = "更新人")
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    public String updateBy;

    /**
     * 更新者id
     */
    @Schema(description = "更新人id")
    @TableField(value = "update_by_id", fill = FieldFill.INSERT_UPDATE)
    public String updateById;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    public LocalDateTime updateTime;

    /**
     * 状态 1正常 2异常
     */
    @Schema(description = "状态（1 正常 2 异常）")
    @TableField(value = "status", fill = FieldFill.INSERT)
    public Integer status;
}