package top.teek.encrypt.filter;

import cn.hutool.core.util.ObjectUtil;
import top.teek.core.exception.ServiceException;
import top.teek.core.http.ResponseStatusEnum;
import top.teek.encrypt.annotation.ApiEncrypt;
import top.teek.encrypt.properties.ApiDecryptProperties;
import top.teek.helper.SpringHelper;
import top.teek.utils.StringUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2024/6/8 23:35:13
 * @note 过滤器
 */
public class CryptoFilter implements Filter {
    private final ApiDecryptProperties properties;

    public CryptoFilter(ApiDecryptProperties properties) {
        this.properties = properties;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        // 获取加密注解
        ApiEncrypt apiEncrypt = getApiEncryptAnnotation(servletRequest);
        boolean requestFlag = Objects.nonNull(apiEncrypt) && apiEncrypt.request();
        boolean responseFlag = Objects.nonNull(apiEncrypt) && apiEncrypt.response();
        ServletRequest requestWrapper = null;
        ServletResponse responseWrapper = null;
        EncryptResponseBodyWrapper responseBodyWrapper = null;

        // 是否执行解密 && 是否为 put 或者 post 请求
        if (requestFlag && (HttpMethod.PUT.matches(servletRequest.getMethod()) || HttpMethod.POST.matches(servletRequest.getMethod()))) {
            // 是否存在解密标头
            String headerValue = servletRequest.getHeader(properties.getHeaderFlag());
            if (StringUtil.hasText(headerValue)) {
                // 请求解密
                requestWrapper = new DecryptRequestBodyWrapper(servletRequest, properties.getPrivateKey(), properties.getHeaderFlag());
            } else if (requestFlag) {
                // 是否有注解，有就报错，没有放行
                HandlerExceptionResolver exceptionResolver = SpringHelper.getBean("handlerExceptionResolver", HandlerExceptionResolver.class);
                exceptionResolver.resolveException(
                        servletRequest, servletResponse, null,
                        new ServiceException(ResponseStatusEnum.REQ_REJECT.getCode(), ResponseStatusEnum.REQ_REJECT.getStatus(), "没有访问权限，请联系管理员授权"));
                return;
            }
        }

        // 判断是否响应加密
        if (responseFlag) {
            responseBodyWrapper = new EncryptResponseBodyWrapper(servletResponse);
            responseWrapper = responseBodyWrapper;
        }

        chain.doFilter(
                ObjectUtil.defaultIfNull(requestWrapper, request),
                ObjectUtil.defaultIfNull(responseWrapper, response));

        if (responseFlag) {
            servletResponse.reset();
            // 对原始内容加密
            String encryptContent = responseBodyWrapper.getEncryptContent(
                    servletResponse, properties.getPublicKey(), properties.getHeaderFlag());
            // 对加密后的内容写出
            servletResponse.getWriter().write(encryptContent);
        }
    }

    /**
     * 获取 ApiEncrypt 注解
     */
    private ApiEncrypt getApiEncryptAnnotation(HttpServletRequest servletRequest) {
        RequestMappingHandlerMapping handlerMapping = SpringHelper.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        // 获取注解
        try {
            HandlerExecutionChain mappingHandler = handlerMapping.getHandler(servletRequest);
            if (Objects.nonNull(mappingHandler)) {
                Object handler = mappingHandler.getHandler();
                // 从 handler 获取注解
                if (handler instanceof HandlerMethod handlerMethod) {
                    return handlerMethod.getMethodAnnotation(ApiEncrypt.class);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void destroy() {
    }
}
