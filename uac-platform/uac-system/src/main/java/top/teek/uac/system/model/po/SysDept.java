package top.teek.uac.system.model.po;

import top.teek.mp.annotation.FieldValueFill;
import top.teek.mp.annotation.ValueStrategy;
import top.teek.mp.base.BaseDO;
import top.teek.uac.system.model.vo.SysDeptVO;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author Teeker
 * @date 2023-21-12 00:21:11
 * @note 部门信息
*/
@TableName("t_sys_dept")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysDeptVO.class)
public class SysDept extends BaseDO {
    /**
     * 部门 ID
     */
    @TableField(fill = FieldFill.INSERT)
    @FieldValueFill(ValueStrategy.SNOWFLAKE)
    private String deptId;

    /**
     * 父级部门 ID
     */
    private String parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 部门名
     */
    private String deptName;

    /**
     * 部门图标
     */
    private String icon;

    /**
     * 部门显示顺序
     */
    private Integer orderNum;

    /**
     * 部门用户数量
     */
    private Integer userCount;

    /**
     * 部门负责人
     */
    private String leader;

    /**
     * 负责电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 部门介绍
     */
    private String intro;

    /**
     * 部门层级
     */
    private Integer level;

    /**
     * 租户编号
     */
    private String tenantId;

    /**
     * 是否被选中
     */
    @TableField(exist = false)
    private boolean selected;
}