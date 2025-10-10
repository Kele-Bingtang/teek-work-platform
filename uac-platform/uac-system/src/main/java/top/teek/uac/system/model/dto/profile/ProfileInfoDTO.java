package top.teek.uac.system.model.dto.profile;

import top.teek.uac.system.model.dto.SysUserDTO;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Teeker
 * @date 2024/4/28 00:45:54
 * @note
 */
@Data
@AutoMapper(target = SysUserDTO.class, reverseConvertGenerate = false)
public class ProfileInfoDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户昵称
     */
    @Size(min = 0, max = 30, message = "用户昵称长度不能超过{max}个字符")
    private String nickname;

    /**
     * 手机密码
     */
    @Size(min = 0, max = 30, message = "用户昵称长度不能超过{max}个字符")
    private String phone;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过{max}个字符")
    private String email;

    /**
     * 性别
     */
    private Integer sex;
}
