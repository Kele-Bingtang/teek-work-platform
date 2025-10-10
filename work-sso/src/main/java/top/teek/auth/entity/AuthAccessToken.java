package top.teek.auth.entity;

import top.teek.mp.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Teeker
 * @date 2023/9/24 2:41
 * @note
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@TableName( "work_sso.t_access_token")
public class AuthAccessToken extends BaseDO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * appId
     */
    @TableField("app_id")
    private String appId;
    /**
     * 用户类型
     */
    @TableField("user_type")
    private Integer userType;
    /**
     * 访问令牌
     */
    @TableField("access_token")
    private String accessToken;
    /**
     * 刷新令牌
     */
    @TableField("refresh_token")
    private String refreshToken;
    /**
     * 授权范围
     */
    @TableField("scopes")
    private List<String> scopes;
    /**
     * 过期时间
     */
    @TableField("expires_time")
    private LocalDateTime expiresTime;
    /**
     * 缓存过期时间
     */
    @TableField("cache_expires_time")
    private Long cacheExpiresTime;
}