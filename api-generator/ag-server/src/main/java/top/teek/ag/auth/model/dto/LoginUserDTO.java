package top.teek.ag.auth.model.dto;

import top.teek.ag.core.bo.LoginUserBO;
import top.teek.ag.core.constant.AuthConstant;
import top.teek.core.validate.AuthGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Teeker
 * @date 2024/6/22 16:12:52
 * @note
 */
@Data
@AutoMapper(target = LoginUserBO.class, reverseConvertGenerate = false)
public class LoginUserDTO {
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
}
