package top.teek.uac.system.model.dto.link;

import top.teek.core.validate.RestGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Teeker
 * @date 2024/3/14 22:51
 * @note
 */
@Data
@Accessors(chain = true)
public class UserLinkRoleDTO {
    /**
     * 主键 ID
     */
    @NotNull(message = "主键 ID 不能为空", groups = {RestGroup.EditGroup.class, RestGroup.DeleteGroup.class})
    private Long id;

    /**
     * 用户 ID
     */
    @NotBlank(message = "用户 ID 不能为空", groups = {RestGroup.AddGroup.class})
    private String userId;

    /**
     * 角色 ID
     */
    @NotNull(message = "角色 ID 不能为空", groups = {RestGroup.AddGroup.class})
    private List<String> roleIds;

    /**
     * 生效时间
     */
    @NotNull(message = "生效时间不能为空", groups = {RestGroup.AddGroup.class})
    private LocalDate validFrom;

    /**
     * 过期时间
     */
    @NotNull(message = "过期时间不能为空", groups = {RestGroup.AddGroup.class})
    private LocalDate expireOn;

    /**
     * 应用 ID
     */
    @NotNull(message = "应用 ID 不能为空", groups = {RestGroup.AddGroup.class})
    private String appId;
}
