package top.teek.auth.vo;

import lombok.*;

/**
 * @author Teeker
 * @date 2023/9/24 2:48
 * @note
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenApiTokenVO {
    /**
     * 访问 token
     */
    private String accessToken;
    /**
     * 刷新 token
     */
    private String refreshToken;
    /**
     * 令牌类型
     */
    private String tokenType;
    /**
     * 过期时间
     */
    private Long expireIn;
}