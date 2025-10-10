package top.teek.uac.core.error;

import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.core.http.ResponseStatusEnum;
import jakarta.servlet.Servlet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author Teeker
 * @date 2024/4/23 20:46:56
 * @note
 */
@RestControllerAdvice
@ResponseBody
@Slf4j
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public Response<Object> handleError(AccessDeniedException e) {
        log.error("请求没有权限访问: {}", e.getMessage());
        return HttpResult.error(ResponseStatusEnum.REQ_REJECT, e.getMessage() + "：请求没有权限访问");
    }
}
