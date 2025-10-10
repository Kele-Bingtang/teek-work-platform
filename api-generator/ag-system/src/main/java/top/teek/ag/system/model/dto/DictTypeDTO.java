package top.teek.ag.system.model.dto;

import top.teek.ag.system.model.po.DictType;
import top.teek.core.validate.RestGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Teeker
 * @date 2023/12/4 16:29
 * @note
 */
@Data
@AutoMapper(target = DictType.class, reverseConvertGenerate = false)
public class DictTypeDTO {
    /**
     * id
     */
    @NotNull(message = "id 不能为空", groups = {RestGroup.EditGroup.class, RestGroup.DeleteGroup.class})
    public Long id;
    /**
     * 字典 ID
     */
    private String dictId;
    /**
     * 字典类型
     */
    @NotBlank(message = "字典类型不能为空", groups = {RestGroup.AddGroup.class})
    @Size(min = 0, max = 100, message = "字典类型类型长度不能超过 {max} 个字符", groups = {RestGroup.AddGroup.class, RestGroup.EditGroup.class})
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "字典类型必须以字母开头，且只能为（小写字母，数字，下滑线）", groups = {RestGroup.AddGroup.class})
    private String dictCode;

    /**
     * 字典名称
     */
    @NotBlank(message = "字典名称不能为空", groups = {RestGroup.AddGroup.class})
    @Size(min = 0, max = 100, message = "字典类型名称长度不能超过 {max} 个字符", groups = {RestGroup.AddGroup.class, RestGroup.EditGroup.class})
    private String dictName;

    /**
     * 是否开启级联（0 不开启，1 开启）
     */
    private Integer isCascade;

    /**
     * 介绍
     */
    private String intro;
}
