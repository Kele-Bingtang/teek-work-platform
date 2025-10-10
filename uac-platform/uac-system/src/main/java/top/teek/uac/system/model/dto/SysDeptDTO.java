package top.teek.uac.system.model.dto;

import top.teek.core.validate.RestGroup;
import top.teek.uac.system.model.po.SysDept;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Teeker
 * @date 2023-21-12 00:21:11
 * @note 部门信息
 */
@Data
@AutoMapper(target = SysDept.class, reverseConvertGenerate = false)
public class SysDeptDTO {
    /**
     * id
     */
    @NotNull(message = "id 不能为空", groups = {RestGroup.EditGroup.class, RestGroup.DeleteGroup.class})
    public Long id;
    /**
     * 部门 ID
     */
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
    @NotBlank(message = "部门名称不能为空", groups = {RestGroup.AddGroup.class})
    @Size(min = 0, max = 30, message = "部门名称长度不能超过 {max} 个字符", groups = {RestGroup.AddGroup.class})
    private String deptName;

    /**
     * 部门图标
     */
    private String icon;

    /**
     * 部门显示顺序
     */
    @NotNull(message = "显示顺序不能为空", groups = {RestGroup.AddGroup.class})
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
     * 负责人电话
     */
    @Size(min = 0, max = 11, message = "联系电话长度不能超过 {max} 个字符", groups = {RestGroup.AddGroup.class, RestGroup.EditGroup.class})
    private String phone;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确", groups = {RestGroup.AddGroup.class, RestGroup.EditGroup.class})
    @Size(min = 0, max = 50, message = "邮箱长度不能超过 {max} 个字符", groups = {RestGroup.AddGroup.class, RestGroup.EditGroup.class})
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
     * 状态（0 异用 1 正常）
     */
    private Integer status;

}