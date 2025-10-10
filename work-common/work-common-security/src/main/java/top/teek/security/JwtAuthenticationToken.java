package top.teek.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;

/**
 * @author Teeker
 * @date 2022/12/10 21:44
 * @note 相比较自带的 UsernamePasswordAuthenticationToken 类，多了 token 属性
 * 这个类用于返回给客户端，包含用户的用户名、密码、token
 */
@Setter
@Getter
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    @Serial
    private static final long serialVersionUID = 1L;

    private String accessToken;
    private String refreshToken;
    private Authentication authentication;
    
    public JwtAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public JwtAuthenticationToken(Object principal, Object credentials, String accessToken, String refreshToken) {
        super(principal, credentials);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String accessToken, String refreshToken) {
        super(principal, credentials, authorities);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}