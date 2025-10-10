package top.teek.security.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

/**
 * @author Teeker
 * @date 2023/11/16 0:52
 * @note Spring Security 认证后内部存储的类
 */
@Setter
@Getter
public class SecurityUser extends User {
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 用户性别（0 未知 1 男 2 女）
     */
    private Integer sex;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 注册时间
     */
    private LocalDateTime registerTime;

    /**
     * 角色码
     */
    private Set<String> roleCodes;

    /**
     * 菜单权限
     */
    private Set<String> menuPermission;

    public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.username = username;
    }
}
