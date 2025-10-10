package top.teek.ag.system.model.dto;

import top.teek.ag.system.model.po.ProjectMember;
import top.teek.core.validate.RestGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Teeker
 * @date 2024/6/23 23:53:50
 * @note
 */
@Data
@AutoMapper(target = ProjectMember.class, reverseConvertGenerate = false)
@Accessors(chain = true)
public class ProjectMemberDTO {
    /**
     * 主键
     */
    @NotNull(message = "id 不能为空", groups = {RestGroup.EditGroup.class, RestGroup.DeleteGroup.class})
    private Long id;

    /**
     * 用户名
     */
    private String userId;

    /**
     * 项目 ID
     */
    private String projectId;

    /**
     * 项目角色（1 管理员 2 普通成员 3 只读成员 4 禁止访问）
     */
    private Integer projectRole;

    /**
     * 0 项目创建者 1 项目加入者
     */
    private Integer belongType;

    /**
     * 团队 ID
     */
    @NotBlank(message = "团队 ID 不能为空", groups = {RestGroup.AddGroup.class, RestGroup.EditGroup.class, RestGroup.QueryGroup.class})
    private String teamId;
}
