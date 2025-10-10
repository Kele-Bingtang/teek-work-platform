package top.teek.auth.entity;

import top.teek.mp.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Teeker
 * @date 2023/9/24 2:43
 * @note
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@TableName( "work_sso.t_refresh_token")
public class AuthRefreshToken extends BaseDO {
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
     * 刷新令牌
     */
    @TableField("refresh_token")
    private String refreshToken;
    /**
     * 用户类型
     */
    @TableField("user_type")
    private Integer userType;
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
}