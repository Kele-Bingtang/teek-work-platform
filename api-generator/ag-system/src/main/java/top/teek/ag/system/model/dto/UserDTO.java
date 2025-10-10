package top.teek.ag.system.model.dto;

import top.teek.ag.system.model.po.User;
import top.teek.core.validate.RestGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2024/6/22 16:53:33
 * @note
 */
@Data
@Accessors(chain = true)
@AutoMapper(target = User.class, reverseConvertGenerate = false)
public class UserDTO {

    /**
     * ID
     */
    @NotNull(message = "id 不能为空", groups = { RestGroup.EditGroup.class, RestGroup.DeleteGroup.class })
    private Long id;

    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 用户名
     */
    @NotBlank(message = "用户账号不能为空", groups = { RestGroup.AddGroup.class })
    @Size(min = 0, max = 30, message = "用户账号长度不能超过 {max} 个字符", groups = { RestGroup.AddGroup.class, RestGroup.EditGroup.class })
    private String username;

    /**
     * 用户昵称
     */
    @Size(min = 0, max = 30, message = "用户昵称长度不能超过 {max} 个字符", groups = { RestGroup.AddGroup.class, RestGroup.EditGroup.class })
    private String nickname;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = { RestGroup.AddGroup.class })
    private String password;

    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空", groups = { RestGroup.AddGroup.class })
    private String repeatPassword;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确", groups = { RestGroup.AddGroup.class })
    @Size(min = 0, max = 50, message = "邮箱长度不能超过 {max} 个字符", groups = { RestGroup.AddGroup.class, RestGroup.EditGroup.class })
    private String email;

    /**
     * 性别（0 保密 1 男 2 女）
     */
    private Integer sex;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 状态（0 离线 1 在线）
     */
    private Integer userStatus;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 注册时间
     */
    private LocalDateTime registerTime;

    /**
     * 最后登录 IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 状态（0 异用 1 正常）
     */
    private Integer status;
}
