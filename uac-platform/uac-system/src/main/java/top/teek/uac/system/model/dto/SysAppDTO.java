package top.teek.uac.system.model.dto;

import top.teek.core.validate.RestGroup;
import top.teek.uac.system.model.po.SysApp;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Teeker
 * @date 2023-20-12 00:20:10
 * @note 应用信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AutoMapper(target = SysApp.class, reverseConvertGenerate = false)
public class SysAppDTO {
    /**
     * id
     */
    @NotNull(message = "id 不能为空", groups = {RestGroup.EditGroup.class, RestGroup.DeleteGroup.class})
    public Long id;
    /**
     * 应用 ID
     */
    private String appId;

    /**
     * 应用码
     */
    @NotBlank(message = "应用编码不能为空", groups = {RestGroup.AddGroup.class})
    private String appCode;

    /**
     * 应用名
     */
    @NotBlank(message = "应用名称不能为空", groups = {RestGroup.AddGroup.class})
    private String appName;

    /**
     * 应用介绍
     */
    private String intro;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    private Integer orderNum;

    /**
     * 负责人 ID
     */
    private String ownerId;

    /**
     * 负责人 username
     */
    private String ownerName;

    /**
     * 部门 ID
     */
    private String deptId;

    /**
     * 客户端 ID
     */
    @NotBlank(message = "客户端 ID 不能为空", groups = {RestGroup.AddGroup.class})
    private String clientId;

    /**
     * 状态（0 异用 1 正常）
     */
    private Integer status;

}