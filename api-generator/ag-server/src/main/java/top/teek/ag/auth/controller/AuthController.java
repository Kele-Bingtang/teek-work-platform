package top.teek.ag.auth.controller;

import top.teek.ag.auth.model.dto.LoginUserDTO;
import top.teek.ag.auth.model.vo.LoginVO;
import top.teek.ag.auth.service.LoginService;
import top.teek.ag.core.helper.AgHelper;
import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.encrypt.annotation.ApiEncrypt;
import top.teek.security.domain.LoginUser;
import top.teek.security.enumeration.AuthErrorCodeEnum;
import top.teek.security.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author Teeker
 * @date 2024/6/22 16:10:46
 * @note
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final LoginService loginService;
    
    @PostMapping("/login")
    @Operation(summary = "执行登录")
    @ApiEncrypt(request = true, response = false)
    public Response<LoginVO> login(@Valid @RequestBody LoginUserDTO loginUserDTO) {
        return HttpResult.ok(loginService.login(loginUserDTO));
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public Response<String> logout() {
        loginService.logout();
        return HttpResult.ok("退出成功");
    }

    @GetMapping("/getUserInfo")
    @Operation(summary = "获取用户信息")
    public Response<LoginUser> getUserInfo() {
        if ("anonymousUser".equals(SecurityUtils.getUsername())) {
            return HttpResult.failMessage("您没有登录！");
        }
        // 获取登录的用户信息
        LoginUser loginUser = AgHelper.getLoginUser();

        if (Objects.isNull(loginUser)) {
            return HttpResult.fail(AuthErrorCodeEnum.UN_AUTHORIZED);
        }
        return HttpResult.ok(loginUser);
    }
}
