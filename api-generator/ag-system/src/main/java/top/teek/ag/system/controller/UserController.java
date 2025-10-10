package top.teek.ag.system.controller;

import top.teek.ag.core.helper.AgHelper;
import top.teek.ag.system.model.dto.UserDTO;
import top.teek.ag.system.model.dto.UserPasswordDTO;
import top.teek.ag.system.model.po.User;
import top.teek.ag.system.model.vo.UserVO;
import top.teek.ag.system.service.UserService;
import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.core.validate.RestGroup;
import top.teek.idempotent.annotation.PreventRepeatSubmit;
import top.teek.security.domain.LoginUser;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2024/7/28 18:39:22
 * @note
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/listByKeyword/{keyword}")
    @Operation(summary = "通过关键字查询用户", description = "通过关键字查询用户")
    public Response<List<UserVO>> listByKeyword(@PathVariable String keyword) {
        List<UserVO> userVOList = userService.listByKeyword(keyword);
        return HttpResult.ok(userVOList);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "用户注册")
    public Response<Boolean> register(@Validated(RestGroup.AddGroup.class) @RequestBody UserDTO userDTO) {
        if (userService.checkUsernameUnique(userDTO)) {
            return HttpResult.failMessage("用户名已存在");
        }
        
        if (!userDTO.getPassword().equals(userDTO.getRepeatPassword())) {
            return HttpResult.failMessage("两次密码不一致");
        }

        return HttpResult.ok(userService.register(userDTO));
    }

    @PutMapping
    @Operation(summary = "个人信息修改", description = "修改个人信息")
    @PreventRepeatSubmit
    public Response<Boolean> editUser(@Validated @RequestBody UserDTO userDTO) {
        LoginUser loginUser = AgHelper.getLoginUser();
        if (Objects.isNull(loginUser)) {
            return HttpResult.failMessage("用户信息失效");
        }
        User user = MapstructUtil.convert(userDTO, User.class);

        user.setId(loginUser.getId());
        user.setUserId(loginUser.getUserId());
        
        if (StringUtil.hasText(user.getPhone()) && userService.checkPhoneUnique(userDTO)) {
            return HttpResult.failMessage("修改用户「" + userDTO.getNickname() + "」失败，手机号「" + userDTO.getPhone() + "」已存在");
        }
        if (StringUtil.hasText(userDTO.getEmail()) && userService.checkEmailUnique(userDTO)) {
            return HttpResult.failMessage("修改用户「" + userDTO.getNickname() + "」失败，邮箱账号「" + userDTO.getEmail() + "」已存在");
        }
        
        boolean result = userService.editUser(userDTO);
        return HttpResult.ok(result);
    }

    @PutMapping("/updatePassword")
    @Operation(summary = "个人密码修改", description = "修改个人密码")
    public Response<Boolean> updatePassword(@Validated @RequestBody UserPasswordDTO userPasswordDTO) {
        LoginUser loginUser = AgHelper.getLoginUser();
        if (Objects.isNull(loginUser)) {
            return HttpResult.failMessage("用户未登录");
        }
        User sysUser = userService.getById(loginUser.getId());
        if (!passwordEncoder.matches(userPasswordDTO.getOldPassword(), sysUser.getPassword())) {
            return HttpResult.failMessage("修改密码失败，旧密码错误");
        }

        if (passwordEncoder.matches(userPasswordDTO.getNewPassword(), sysUser.getPassword())) {
            return HttpResult.failMessage("新密码不能与旧密码相同");
        }

        boolean result = userService.updatePassword(loginUser.getUserId(), passwordEncoder.encode(userPasswordDTO.getNewPassword()));
        return HttpResult.ok(result);
    }
}
