package top.teek.uac.system.model.dto;

import top.teek.core.validate.RestGroup;
import top.teek.uac.system.model.po.SysUserGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Teeker
 * @date 2024/3/13 0:19
 * @note
 */
@Data
@AutoMapper(target = SysUserGroup.class, reverseConvertGenerate = false)
public class SysUserGroupDTO {

    @NotNull(message = "id 不能为空", groups = { RestGroup.EditGroup.class, RestGroup.DeleteGroup.class })
    private Long id;
    
    /**
     * 用户组 ID
     */
    private String groupId;

    /**
     * 用户组名
     */
    @NotBlank(message = "用户组名不能为空", groups = {RestGroup.AddGroup.class})
    private String groupName;

    /**
     * 用户组描述
     */
    private String intro;

    /**
     * 负责人 ID
     */
    private String ownerId;

    /**
     * 负责人 username
     */
    private String ownerName;

    /**
     * 应用 ID
     */
    @NotBlank(message = "应用 ID 不能为空", groups = {RestGroup.AddGroup.class})
    private String appId;
}
