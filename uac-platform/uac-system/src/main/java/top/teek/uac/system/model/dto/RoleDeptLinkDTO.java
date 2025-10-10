package top.teek.uac.system.model.dto;

import top.teek.core.validate.RestGroup;
import top.teek.uac.system.model.po.RoleDeptLink;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Teeker
 * @date 2023/12/16 0:45
 * @note
 */
@Data
@AutoMapper(target = RoleDeptLink.class, reverseConvertGenerate = false)
public class RoleDeptLinkDTO {
    @NotBlank(message = "id 不能为空", groups = {RestGroup.EditGroup.class, RestGroup.DeleteGroup.class})
    private Long id;
    
    @NotBlank(message = "角色 ID 不能为空", groups = {RestGroup.AddGroup.class})
    private String roleId;

    @NotBlank(message = "部门 ID 不能为空", groups = {RestGroup.AddGroup.class})
    private String deptId;

    @NotBlank(message = "应用 ID 不能为空", groups = {RestGroup.QueryGroup.class, RestGroup.AddGroup.class})
    private String appId;
}
