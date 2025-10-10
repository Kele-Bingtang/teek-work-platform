package top.teek.uac.auth.controller;

import top.teek.core.constants.ColumnConstant;
import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.encrypt.annotation.ApiEncrypt;
import top.teek.security.domain.LoginUser;
import top.teek.security.enumeration.AuthErrorCodeEnum;
import top.teek.security.utils.SecurityUtils;
import top.teek.tenant.helper.TenantHelper;
import top.teek.uac.auth.model.dto.LoginUserDTO;
import top.teek.uac.auth.model.vo.LoginTenantSelectVO;
import top.teek.uac.auth.model.vo.LoginVO;
import top.teek.uac.auth.model.vo.TenantSelectVO;
import top.teek.uac.auth.model.vo.UserInfoVO;
import top.teek.uac.auth.service.LoginService;
import top.teek.uac.core.helper.UacHelper;
import top.teek.uac.system.model.dto.SysTenantDTO;
import top.teek.uac.system.model.po.SysApp;
import top.teek.uac.system.model.po.SysClient;
import top.teek.uac.system.model.vo.SysTenantVO;
import top.teek.uac.system.service.SysAppService;
import top.teek.uac.system.service.SysClientService;
import top.teek.uac.system.service.SysTenantService;
import top.teek.utils.ListUtil;
import top.teek.utils.MapstructUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2023/11/12 14:14
 * @note 认证 Controller
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final SysAppService appService;
    private final SysClientService clientService;
    private final LoginService loginService;
    private final SysTenantService sysTenantService;

    @PostMapping("/login")
    @Operation(summary = "执行登录")
    @ApiEncrypt(request = true, response = false)
    public Response<LoginVO> login(@Valid @RequestBody LoginUserDTO loginUserDTO) {
        String appId = loginUserDTO.getAppId();
        // 校验 App ID
        SysApp sysApp = appService.checkAppIdThenGet(loginUserDTO.getTenantId(), appId);
        if (Objects.isNull(sysApp)) {
            log.info("应用[{}]不存在", appId);
            return HttpResult.failMessage("应用[" + appId + "]不存在");
        }
        String appName = sysApp.getAppName();
        if (!ColumnConstant.STATUS_NORMAL.equals(sysApp.getStatus())) {
            log.info("[{}]已被禁用", appName);
            return HttpResult.failMessage("[" + appName + "]已被禁用");
        }
        // 校验客户端
        SysClient sysClient = clientService.checkClientIdThenGet(sysApp.getClientId());
        String clientName = sysClient.getClientName();
        if (!ColumnConstant.STATUS_NORMAL.equals(sysClient.getStatus())) {
            log.info("客户端[{}]已被禁用.", clientName);
            return HttpResult.failMessage("客户端[" + clientName + "]已被禁用");
        }
        // 校验租户 ID
        loginService.checkTenant(loginUserDTO.getTenantId());
        // 执行登录
        return HttpResult.ok(loginService.login(loginUserDTO, sysApp, sysClient));
    }
    
    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public Response<String> logout() {
        loginService.logout();
        return HttpResult.ok("退出成功");
    }

    @GetMapping("/tenant/list")
    @Operation(summary = "多租户下拉选项")
    public Response<LoginTenantSelectVO> tenantSelectOption(HttpServletRequest request) {
        List<SysTenantVO> sysTenantVOList = sysTenantService.listAll(new SysTenantDTO());
        List<TenantSelectVO> tenantSelectVOList = MapstructUtil.convert(sysTenantVOList, TenantSelectVO.class);

        // 获取域名
        String serverName = request.getServerName();
        List<TenantSelectVO> list = ListUtil.newArrayList();

        // 本地不需要过滤
        if ("localhost".equals(serverName)) {
            list = tenantSelectVOList;
        } else if (!ListUtil.isEmpty(tenantSelectVOList)) {
            // 过滤出租户所在的域名
            list = tenantSelectVOList.stream().filter(tenantSelectVo -> serverName.equals(tenantSelectVo.getDomain())).toList();
        }

        LoginTenantSelectVO loginTenantSelectVo = LoginTenantSelectVO.builder()
                .tenantEnabled(TenantHelper.isEnable())
                .tenantSelectList(list)
                .build();

        return HttpResult.ok(loginTenantSelectVo);
    }
    
    @GetMapping("/getUserInfo")
    @Operation(summary = "获取用户信息")
    public Response<UserInfoVO> getUserInfo() {
        if ("anonymousUser".equals(SecurityUtils.getUsername())) {
            return HttpResult.failMessage("您没有登录！");
        }
        // 获取登录的用户信息
        LoginUser loginUser = UacHelper.getLoginUser();

        if (Objects.isNull(loginUser)) {
            return HttpResult.fail(AuthErrorCodeEnum.UN_AUTHORIZED);
        }

        UserInfoVO userInfoVo = new UserInfoVO();

        userInfoVo.setUser(loginUser)
                .setRoleCodes(loginUser.getRoleCodes())
                .setPermissions(loginUser.getMenuPermission());
        return HttpResult.ok(userInfoVo);
    }
}
