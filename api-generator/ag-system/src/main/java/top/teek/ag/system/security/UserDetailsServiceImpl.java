package top.teek.ag.system.security;

import top.teek.ag.core.exception.ApiGeneratorException;
import top.teek.ag.system.model.po.User;
import top.teek.ag.system.service.UserService;
import top.teek.core.constants.ColumnConstant;
import top.teek.security.domain.SecurityUser;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Teeker
 * @date 2024/6/22 15:48:45
 * @note
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl  implements UserDetailsService {
    
    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, username));

        if (Objects.isNull(user)) {
            log.info("用户不存在");
            // 在 LoginFailureHandler 以 InternalAuthenticationServiceException 异常被捕获
            throw new ApiGeneratorException("用户不存在");
        }

        if (ColumnConstant.STATUS_EXCEPTION.equals(user.getStatus())) {
            log.info("用户已被停用");
            throw new ApiGeneratorException("用户已被停用");
        }

        SecurityUser securityUser = new SecurityUser(user.getUsername(), user.getPassword(), AuthorityUtils.NO_AUTHORITIES);
        securityUser.setId(user.getId());
        securityUser.setUserId(user.getUserId());
        securityUser.setNickname(user.getNickname());
        securityUser.setEmail(user.getEmail());
        securityUser.setPhone(user.getPhone());
        securityUser.setSex(user.getSex());
        securityUser.setBirthday(user.getBirthday());
        securityUser.setAvatar(user.getAvatar());
        securityUser.setRegisterTime(user.getRegisterTime());
        securityUser.setLoginTime(user.getLoginTime());
        securityUser.setLoginIp(user.getLoginIp());

        return securityUser;
    }
}
