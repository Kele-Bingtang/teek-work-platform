package top.teek.ag.system.model.dto;

import top.teek.ag.system.model.po.TeamMember;
import top.teek.core.validate.RestGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Teeker
 * @date 2024/6/22 15:30:31
 * @note
 */
@Data
@Accessors(chain = true)
@AutoMapper(target = TeamMember.class, reverseConvertGenerate = false)
public class TeamMemberDTO {
    /**
     * 主键
     */
    @NotNull(message = "id 不能为空", groups = {RestGroup.EditGroup.class, RestGroup.DeleteGroup.class})
    private Long id;
    
    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;
    
    /**
     * 团队内昵称
     */
    private String nickname;

    /**
     * 团队角色（1 所有者 2 管理员 3 普通成员）
     */
    private Integer teamRole;


    /**
     * 1 团队创建者 2 团队加入者
     */
    private Integer belongType;

    /**
     * 状态（0 异常 1 正常）
     */
    private Integer status;

    /**
     * 团队 ID
     */
    @NotBlank(message = "团队 ID 不能为空", groups = {RestGroup.AddGroup.class, RestGroup.EditGroup.class, RestGroup.QueryGroup.class})
    private String teamId;
    
}
