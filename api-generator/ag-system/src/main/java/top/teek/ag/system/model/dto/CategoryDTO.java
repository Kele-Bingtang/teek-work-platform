package top.teek.ag.system.model.dto;

import top.teek.ag.system.model.po.Category;
import top.teek.core.validate.RestGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Teeker
 * @date 2024/6/24 23:54:23
 * @note
 */
@Data
@AutoMapper(target = Category.class, reverseConvertGenerate = false)
@Accessors(chain = true)
public class CategoryDTO {
    /**
     * id
     */
    @NotNull(message = "id 不能为空", groups = {RestGroup.EditGroup.class})
    private Integer id;
    /**
     * 分类 ID
     */
    private String categoryId;

    /**
     * 目录编码
     */
    @NotBlank(message = "目录编码不能为空", groups = {RestGroup.AddGroup.class, RestGroup.EditGroup.class})
    private String categoryCode;

    /**
     * 目录名称
     */
    @NotBlank(message = "目录名称不能为空", groups = {RestGroup.AddGroup.class, RestGroup.EditGroup.class})
    private String categoryName;

    /**
     * 是否是主目录（0 不是 1 是）
     */
    private Integer isMain;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 项目 id
     */
    @NotBlank(message = "项目 ID 不能为空", groups = {RestGroup.AddGroup.class, RestGroup.EditGroup.class})
    private String projectId;

    /**
     * 团队 ID
     */
    @NotBlank(message = "团队 ID 不能为空", groups = {RestGroup.AddGroup.class})
    private String teamId;
}
