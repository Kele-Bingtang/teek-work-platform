package top.teek.web.error;

import top.teek.core.exception.BaseException;
import top.teek.core.exception.ServiceException;
import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.core.http.ResponseStatusEnum;
import top.teek.utils.StringUtil;
import jakarta.servlet.Servlet;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;
import java.util.Set;

/**
 * @author Teeker
 * @date 2023/6/30 22:54
 * @note
 */
@RestControllerAdvice
@Slf4j
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Object> handleError(MissingServletRequestParameterException e) {
        log.warn("缺少请求参数：{}", e.getMessage());
        String message = String.format("缺少必要的请求参数： %s", e.getParameterName());
        return HttpResult.error(ResponseStatusEnum.PARAM_MISS, message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Object> handleError(MethodArgumentTypeMismatchException e) {
        log.warn("请求参数格式错误：{}", e.getMessage());
        String message = String.format("请求参数格式错误： %s", e.getName());
        return HttpResult.fail(ResponseStatusEnum.PARAM_TYPE_ERROR, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Object> handleError(MethodArgumentNotValidException e) {
        log.warn("参数验证失败：{}", e.getMessage());
        return handleError(e.getBindingResult());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Object> handleError(BindException e) {
        log.warn("参数绑定失败：{}", e.getMessage());
        return handleError(e.getBindingResult());
    }

    private Response<Object> handleError(BindingResult result) {
        FieldError error = result.getFieldError();
        if (Objects.isNull(error)) {
            return HttpResult.fail(ResponseStatusEnum.PARAM_BIND_ERROR);
        }
        String message = String.format("%s：%s", error.getField(), error.getDefaultMessage());
        return HttpResult.fail(ResponseStatusEnum.PARAM_BIND_ERROR, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Object> handleError(ConstraintViolationException e) {
        log.warn("参数验证失败：{}", e.getMessage());
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String path = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
        String message = String.format("%s：%s", path, violation.getMessage());
        return HttpResult.fail(ResponseStatusEnum.PARAM_VALID_ERROR, message);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response<Object> handleError(NoHandlerFoundException e) {
        log.error("404 没找到请求：{}", e.getMessage());
        return HttpResult.fail(ResponseStatusEnum.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Object> handleError(HttpMessageNotReadableException e) {
        log.error("消息不能读取：{}", e.getMessage());
        return HttpResult.fail(ResponseStatusEnum.MSG_NOT_READABLE, e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Response<Object> handleError(HttpRequestMethodNotSupportedException e) {
        log.error("不支持当前请求方法：{}", e.getMessage());
        return HttpResult.error(ResponseStatusEnum.METHOD_NOT_SUPPORTED, e.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public Response<Object> handleError(HttpMediaTypeNotSupportedException e) {
        log.error("不支持当前媒体类型：{}", e.getMessage());
        return HttpResult.error(ResponseStatusEnum.MEDIA_TYPE_NOT_SUPPORTED, e.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public Response<Object> handleError(HttpMediaTypeNotAcceptableException e) {
        log.error("媒体类型异常：{}", e.getMessage());
        return HttpResult.error(ResponseStatusEnum.MEDIA_TYPE_NOT_SUPPORTED, e.getMessage());
    }
    
    @ExceptionHandler(ServiceException.class)
    public Response<Object> handleError(ServiceException e) {
        log.error("业务异常：{}", e.getMessage());
        return HttpResult.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BaseException.class)
    public Response<Object> handleError(BaseException e) {
        log.error("业务异常：{}", e.getMessage());
        return HttpResult.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Object> handleError(Throwable e) {
        log.error("服务器异常：{}", Objects.nonNull(e.getMessage()) ? e.getMessage() : e.getCause().getMessage());
        // 发送服务异常事件
        return HttpResult.error(ResponseStatusEnum.INTERNAL_SERVER_ERROR, (Objects.isNull(e.getMessage()) ? ResponseStatusEnum.INTERNAL_SERVER_ERROR.getMessage() : e.getMessage()));
    }

    /**
     * 处理系统异常，兜底处理所有的一切
     */
    @ExceptionHandler(Exception.class)
    public Response<Object> defaultExceptionHandler(Throwable e) {
        // log.error("服务器异常：{}", Objects.nonNull(e.getMessage()) ? e.getMessage() : e.getCause().getMessage());
        // 本地调式，知道在哪里发生异常
        log.error("服务器异常", e);
        if (StringUtil.hasText(e.getMessage())) {
            return HttpResult.errorMessage(e.getMessage());
        } else if (StringUtil.hasText(e.getCause().getMessage())) {
            return HttpResult.errorMessage(e.getCause().getMessage());
        }
        return HttpResult.error(ResponseStatusEnum.INTERNAL_SERVER_ERROR);
    }
}