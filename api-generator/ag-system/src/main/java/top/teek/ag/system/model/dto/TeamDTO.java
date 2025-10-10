package top.teek.ag.system.model.dto;

import top.teek.ag.system.model.po.Team;
import top.teek.core.validate.RestGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Teeker
 * @date 2024/6/22 15:11:01
 * @note
 */
@Data
@AutoMapper(target = Team.class, reverseConvertGenerate = false)
public class TeamDTO {
    /**
     * 主键
     */
    @NotNull(message = "id 不能为空", groups = {RestGroup.EditGroup.class})
    private Long id;

    /**
     * 团队 ID
     */
    @NotNull(message = "id 不能为空", groups = {RestGroup.DeleteGroup.class})
    private String teamId;

    /**
     * 团队名字
     */
    @NotBlank(message = "团队名字不能为空", groups = {RestGroup.AddGroup.class})
    private String teamName;

    /**
     * 团队介绍
     */
    private String description;

    /**
     * 显示顺序
     */
    private Integer orderNum;
}
