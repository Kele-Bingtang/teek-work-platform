package top.teek.uac.system.model.dto;

import top.teek.core.validate.RestGroup;
import top.teek.uac.system.model.po.SysMenu;
import top.teek.uac.system.model.vo.router.Meta;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Teeker
 * @date 2023-23-12 00:23:08
 * @note 菜单
 */
@Data
@AutoMapper(target = SysMenu.class, reverseConvertGenerate = false)
public class SysMenuDTO {
    /**
     * 主键
     */
    @NotNull(message = "id 不能为空", groups = {RestGroup.EditGroup.class, RestGroup.DeleteGroup.class})
    private String id;
    /**
     * 菜单 ID
     */
    private String menuId;

    /**
     * 父级菜单 ID
     */
    private String parentId;

    /**
     * 菜单编码
     */
    @NotBlank(message = "菜单编码不能为空", groups = {RestGroup.AddGroup.class})
    private String menuCode;

    /**
     * 菜单名
     */
    @NotBlank(message = "菜单名称不能为空", groups = {RestGroup.AddGroup.class})
    @Size(min = 0, max = 50, message = "菜单名称长度不能超过 {max} 个字符", groups = {RestGroup.AddGroup.class, RestGroup.EditGroup.class})
    private String menuName;

    /**
     * 菜单地址
     */
    @Size(min = 0, max = 200, message = "路由地址不能超过 {max} 个字符", groups = {RestGroup.AddGroup.class, RestGroup.EditGroup.class})
    private String path;

    /**
     * 图标
     */
    private String icon;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空", groups = {RestGroup.AddGroup.class})
    private Integer orderNum;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 菜单类型（C目录 M菜单 F按钮）
     */
    private String menuType;

    /**
     * 组件路径
     */
    @Size(min = 0, max = 200, message = "组件路径不能超过 {max} 个字符", groups = {RestGroup.AddGroup.class, RestGroup.EditGroup.class})
    private String component;

    /**
     * 菜单前端额外配置
     */
    private Meta meta;

    /**
     * 菜单介绍
     */
    private String intro;

    /**
     * 应用 ID
     */
    @NotBlank(message = "应用 ID 不能为空", groups = {RestGroup.AddGroup.class, RestGroup.QueryGroup.class})
    private String appId;

    /**
     * 状态（0 异用 1 正常）
     */
    private Integer status;


}