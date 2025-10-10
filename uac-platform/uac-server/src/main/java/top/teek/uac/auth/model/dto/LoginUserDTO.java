package top.teek.uac.auth.model.dto;

import top.teek.core.validate.AuthGroup;
import top.teek.uac.core.constant.AuthConstant;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


/**
 * @author Teeker
 * @date 2023/11/12 14:49
 * @note 前端登录时传来的用户信息
 */
@Data
public class LoginUserDTO {
    /**
     * 租户 ID
     */
    @NotBlank(message = "租户 ID 不能为空")
    private String tenantId;
    /**
     * 应用 ID
     */
    @NotBlank(message = "应用 ID 不能为空")
    private String appId;
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空", groups = AuthGroup.PasswordGroup.class)
    @Length(min = AuthConstant.USERNAME_MIN_LENGTH, max = AuthConstant.USERNAME_MAX_LENGTH, message = "用户名长度必须在 {min} - {max} 之间", groups = AuthGroup.PasswordGroup.class)
    private String username;
    /**
     * "用户密码
     */
    @NotBlank(message = "密码不能为空", groups = AuthGroup.PasswordGroup.class)
    @Length(min = AuthConstant.PASSWORD_MIN_LENGTH, max = AuthConstant.PASSWORD_MAX_LENGTH, message = "密码长度必须在 {min} - {max} 之间", groups = AuthGroup.PasswordGroup.class)
    private String password;
    /**
     * 授权类型
     */
    @NotBlank(message = "授权类型不能为空")
    private String grantType;
    
}
