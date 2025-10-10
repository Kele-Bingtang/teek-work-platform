package top.teek.security.domain;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Teeker
 * @date 2024/1/21 23:10
 * @note 登录成功后，存储用户信息的实体类
 */
@Data
@AutoMapper(target = SecurityUser.class, convertGenerate = false)
@Accessors(chain = true)
public class LoginUser {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户 ID
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
     * 登录后的 token
     */
    private String accessToken;

    /**
     * 登录后的刷新 token
     */
    private String refreshToken;

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
     * 客户端名
     */
    private String clientName;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 角色码
     */
    private Set<String> roleCodes;

    /**
     * 菜单权限
     */
    private Set<String> menuPermission;
}
