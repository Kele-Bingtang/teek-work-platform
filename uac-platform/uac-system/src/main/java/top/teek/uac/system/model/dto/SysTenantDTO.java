package top.teek.uac.system.model.dto;

import top.teek.core.validate.RestGroup;
import top.teek.uac.system.model.po.SysTenant;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2023-26-12 00:26:16
 * @note 租户表
 */
@Data
@AutoMapper(target = SysTenant.class, reverseConvertGenerate = false)
public class SysTenantDTO {
    /**
     * id
     */
    @NotNull(message = "id 不能为空", groups = {RestGroup.EditGroup.class, RestGroup.DeleteGroup.class})
    public Long id;

    /**
     * 租户编号
     */
    private String tenantId;

    /**
     * 统一社会信用代码
     */
    private String licenseNumber;

    /**
     * 企业名
     */
    @NotBlank(message = "企业名称不能为空", groups = {RestGroup.AddGroup.class})
    private String tenantName;

    /**
     * 企业图标
     */
    private String icon;

    /**
     * 企业所在地
     */
    private String address;

    /**
     * 企业用户数量（-1 不限制）
     */
    private Integer userCountCapacity;

    /**
     * 用户实际数量
     */
    private Integer userCount;

    /**
     * 企业创始人
     */
    private String founder;

    /**
     * 企业介绍
     */
    private String intro;

    /**
     * 联系人
     */
    @NotBlank(message = "联系人不能为空", groups = {RestGroup.AddGroup.class})
    private String contactUserName;

    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空", groups = {RestGroup.AddGroup.class})
    private String contactPhone;

    /**
     * 用户名
     */
    @NotBlank(message = "用户账号不能为空", groups = {RestGroup.AddGroup.class})
    @Size(min = 0, max = 30, message = "用户账号长度不能超过 {max} 个字符", groups = {RestGroup.AddGroup.class})
    private String username;

    /**
     * 系统用户密码
     */
    @NotBlank(message = "系统用户密码不能为空", groups = {RestGroup.AddGroup.class})
    private String password;

    /**
     * 企业域名
     */
    private String domain;

    /**
     * 租户过期时间（-1 无限期）
     */
    private LocalDateTime expireTime;

    /**
     * 状态（0 异用 1 正常）
     */
    private Integer status;

    /**
     * 应用 ID
     */
    private String appId;

}