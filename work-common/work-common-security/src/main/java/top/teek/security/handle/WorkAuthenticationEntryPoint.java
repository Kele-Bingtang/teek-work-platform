package top.teek.security.handle;

import top.teek.core.http.HttpResult;
import top.teek.core.http.ResponseStatusEnum;
import top.teek.security.enumeration.AuthErrorCodeEnum;
import top.teek.security.utils.JwtTokenUtils;
import top.teek.utils.StringUtil;
import top.teek.web.utils.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author Teeker
 * @date 2023/11/27 23:42
 * @note 匿名访问时，返回错误提示（捕获认证过程中出现的异常）
 */
public class WorkAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        String token = JwtTokenUtils.getToken(request);
        if (StringUtil.hasText(token)) {
            ServletUtil.writeJSON(response, HttpResult.fail(AuthErrorCodeEnum.AUTH_JWT_ERROR));
        } else {
            ServletUtil.writeJSON(response, HttpResult.fail(ResponseStatusEnum.UN_AUTHORIZED));
        }
    }
}
