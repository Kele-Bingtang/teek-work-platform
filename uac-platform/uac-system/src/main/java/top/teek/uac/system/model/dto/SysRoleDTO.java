package top.teek.uac.system.model.dto;

import top.teek.core.validate.RestGroup;
import top.teek.uac.system.model.po.SysRole;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-24-12 00:24:45
 * @note 应用角色信息
 */
@Data
@AutoMapper(target = SysRole.class, reverseConvertGenerate = false)
public class SysRoleDTO {
    /**
     * id
     */
    @NotNull(message = "id 不能为空", groups = {RestGroup.EditGroup.class, RestGroup.DeleteGroup.class})
    public Long id;
    
    /**
     * 角色 ID
     */
    private String roleId;

    /**
     * 角色码
     */
    @NotBlank(message = "角色码不能为空", groups = {RestGroup.AddGroup.class})
    @Size(min = 0, max = 30, message = "角色码长度不能超过 {max} 个字符", groups = {RestGroup.AddGroup.class, RestGroup.EditGroup.class})
    private String roleCode;

    /**
     * 角色名
     */
    @NotBlank(message = "角色名称不能为空", groups = {RestGroup.AddGroup.class})
    @Size(min = 0, max = 30, message = "角色名称长度不能超过{max}个字符", groups = {RestGroup.AddGroup.class, RestGroup.EditGroup.class})
    private String roleName;

    /**
     * 角色介绍
     */
    private String intro;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空", groups = {RestGroup.AddGroup.class})
    private Integer orderNum;

    /**
     * 应用 ID
     */
    @NotBlank(message = "应用 ID 不能为空", groups = {RestGroup.AddGroup.class})
    private String appId;

    /**
     * 状态（0 异用 1 正常）
     */
    private Integer status;

    /**
     * 绑定的菜单 ID
     */
    private List<String> selectedMenuIds;

}