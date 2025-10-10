package top.teek.uac.system.security;

import top.teek.core.constants.ColumnConstant;
import top.teek.security.domain.SecurityUser;
import top.teek.tenant.helper.TenantHelper;
import top.teek.uac.core.constant.TenantConstant;
import top.teek.uac.core.exception.AuthException;
import top.teek.uac.core.helper.UacHelper;
import top.teek.uac.system.model.po.SysUser;
import top.teek.uac.system.model.vo.SysMenuVO;
import top.teek.uac.system.service.SysMenuService;
import top.teek.uac.system.service.SysRoleService;
import top.teek.uac.system.service.SysUserService;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Teeker
 * @note 2022/12/10 21:45
 * @note 该类是 Spring Security 自动调用，判断用户是否存在、有权限
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserService sysUserService;
    private final SysMenuService sysMenuService;
    private final SysRoleService sysRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] loginInfo = username.split(":");
        SysUser user = sysUserService.getOne(Wrappers.<SysUser>lambdaQuery()
                .eq(TenantHelper.isEnable(), SysUser::getTenantId, loginInfo[0])
                .eq(SysUser::getUsername, TenantHelper.isEnable() ? loginInfo[1] : username));

        if (Objects.isNull(user)) {
            log.info("用户不存在");
            // 在 LoginFailureHandler 以 InternalAuthenticationServiceException 异常被捕获
            throw new AuthException("用户不存在");
        }

        if (ColumnConstant.STATUS_EXCEPTION.equals(user.getStatus())) {
            log.info("用户已被停用");
            throw new AuthException("用户已被停用");
        }

        // 查询用户的菜单权限
        List<SysMenuVO> sysMenuVOList = sysMenuService.listMenuListByUserId(TenantConstant.DEFAULT_UAC_APP_ID, user.getUserId());

        List<String> menuPerms = sysMenuVOList.stream().map(SysMenuVO::getPermission).filter(Objects::nonNull).toList();

        List<SimpleGrantedAuthority> authorities = menuPerms.stream()
                .map(SimpleGrantedAuthority::new)
                .distinct()
                .toList();

        SecurityUser securityUser = new SecurityUser(user.getUsername(), user.getPassword(), authorities);
        securityUser.setId(user.getId());
        securityUser.setUserId(user.getUserId());
        securityUser.setDeptId(user.getDeptId());
        securityUser.setTenantId(user.getTenantId());
        securityUser.setNickname(user.getNickname());
        securityUser.setEmail(user.getEmail());
        securityUser.setPhone(user.getPhone());
        securityUser.setSex(user.getSex());
        securityUser.setBirthday(user.getBirthday());
        securityUser.setAvatar(user.getAvatar());
        securityUser.setRegisterTime(user.getRegisterTime());
        securityUser.setLoginTime(user.getLoginTime());
        securityUser.setLoginIp(user.getLoginIp());

        securityUser.setRoleCodes(getRoleCodes(user.getUserId()));
        securityUser.setMenuPermission(getMenuPermission(user.getUserId(), menuPerms));

        return securityUser;

        // 用户权限列表，根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('sys:menu:view')") 标注的接口对比，决定是否可以调用接口
        // List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        // grantedAuthorities.add(new SimpleGrantedAuthority("Kele-Bingtang"));
        // return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }

    private Set<String> getRoleCodes(String userId) {
        Set<String> roleCode = new HashSet<>();
        if(UacHelper.isAdmin(userId)) {
            roleCode.add(TenantConstant.TENANT_ADMIN_ROLE_KEY);
        } else {
            roleCode.addAll(sysRoleService.listRoleCodesByUserId(userId));
        }
        return roleCode;
    }

    private Set<String> getMenuPermission(String userId, List<String> menuPermissions) {
        Set<String> permsSet = new HashSet<>();
        for (String perm : menuPermissions) {
            if (StringUtil.hasText(perm)) {
                permsSet.addAll(List.of(perm.trim().split(",")));
            }
        }

        Set<String> perms = new HashSet<>();
        // 管理员拥有所有权限
        if (UacHelper.isAdmin(userId)) {
            perms.add("*:*:*");
        } else {
            perms.addAll(permsSet);
        }
        return perms;
    }
}