package top.teek.uac.system.controller.system;

import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.idempotent.annotation.PreventRepeatSubmit;
import top.teek.security.domain.LoginUser;
import top.teek.uac.core.helper.UacHelper;
import top.teek.uac.core.log.annotation.OperateLog;
import top.teek.uac.core.log.enums.BusinessType;
import top.teek.uac.system.model.dto.SysUserDTO;
import top.teek.uac.system.model.dto.profile.ProfileInfoDTO;
import top.teek.uac.system.model.dto.profile.UserPasswordDTO;
import top.teek.uac.system.model.po.SysUser;
import top.teek.uac.system.service.SysUserService;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author Teeker
 * @date 2024/4/28 00:42:24
 * @note
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/user/profile")
public class SysProfileController {

    private final SysUserService sysUserService;
    private final PasswordEncoder passwordEncoder;

    @PutMapping
    @OperateLog(title = "个人信息", businessType = BusinessType.UPDATE)
    @PreventRepeatSubmit
    public Response<Boolean> updateProfileInfo(@Validated @RequestBody ProfileInfoDTO profileInfoDTO) {
        LoginUser loginUser = UacHelper.getLoginUser();
        if (Objects.isNull(loginUser)) {
            return HttpResult.failMessage("用户信息失效");
        }
        SysUserDTO sysUserDTO = MapstructUtil.convert(profileInfoDTO, SysUserDTO.class);
        sysUserDTO.setId(loginUser.getId());
        sysUserDTO.setUserId(loginUser.getUserId());
        if (StringUtil.hasText(sysUserDTO.getPhone()) && sysUserService.checkPhoneUnique(sysUserDTO)) {
            return HttpResult.failMessage("修改用户「" + sysUserDTO.getUsername() + "」失败，手机号「" + sysUserDTO.getPhone() + "」已存在");
        }
        if (StringUtil.hasText(sysUserDTO.getEmail()) && sysUserService.checkEmailUnique(sysUserDTO)) {
            return HttpResult.failMessage("修改用户「" + sysUserDTO.getUsername() + "」失败，邮箱账号「" + sysUserDTO.getEmail() + "」已存在");
        }
        boolean result = sysUserService.editUser(sysUserDTO);
        return HttpResult.ok(result);
    }

    @PutMapping("/updatePassword")
    @OperateLog(title = "个人信息", businessType = BusinessType.UPDATE)
    @PreventRepeatSubmit
    public Response<Boolean> updatePassword(@Validated @RequestBody UserPasswordDTO userPasswordDTO) {
        LoginUser loginUser = UacHelper.getLoginUser();
        if (Objects.isNull(loginUser)) {
            return HttpResult.failMessage("用户未登录");
        }
        SysUser sysUser = sysUserService.getById(loginUser.getId());
        if (!passwordEncoder.matches(userPasswordDTO.getOldPassword(), sysUser.getPassword())) {
            return HttpResult.failMessage("修改密码失败，旧密码错误");
        }

        if (passwordEncoder.matches(userPasswordDTO.getNewPassword(), sysUser.getPassword())) {
            return HttpResult.failMessage("新密码不能与旧密码相同");
        }

        boolean result = sysUserService.updatePassword(loginUser.getUserId(), passwordEncoder.encode(userPasswordDTO.getNewPassword()));
        return HttpResult.ok(result);
    }
}
