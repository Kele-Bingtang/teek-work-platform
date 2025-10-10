package top.teek.idempotent.aspect;

import cn.hutool.crypto.SecureUtil;
import top.teek.core.exception.ServiceException;
import top.teek.core.http.Response;
import top.teek.core.http.ResponseStatusEnum;
import top.teek.idempotent.annotation.PreventRepeatSubmit;
import top.teek.redis.utils.RedisUtil;
import top.teek.security.utils.JwtTokenUtils;
import top.teek.utils.JacksonUtil;
import top.teek.web.utils.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Teeker
 * @date 2024/6/9 16:48:15
 * @note
 */
@Aspect
public class PreventRepeatSubmitAspect {

    private static final ThreadLocal<String> KEY_CACHE = new ThreadLocal<>();
    private static final String NO_REPEAT_SUBMIT_KEY = "uac:repeat_submit:";

    /**
     * 拦截请求
     */
    @Before("@annotation(preventRepeatSubmit)")
    public void doBefore(JoinPoint point, PreventRepeatSubmit preventRepeatSubmit) {

        long lockTime = preventRepeatSubmit.timeUnit().toMillis(preventRepeatSubmit.lockTime());

        if (lockTime < 1000) {
            throw new ServiceException("重复提交间隔时间不能小于'1'秒");
        }

        String requestParams = argsArrayToString(point.getArgs());

        HttpServletRequest request = ServletUtil.getRequest();
        if (Objects.isNull(request)) {
            return;
        }
        
        String url = request.getRequestURI();
        // 唯一值
        String token = JwtTokenUtils.getToken(request);
        // 缓存的 key 由 url + MD5(token + ":" + requestParams) 组合
        String cacheKey = NO_REPEAT_SUBMIT_KEY + url + ":" + SecureUtil.md5(token + ":" + requestParams);

        if (RedisUtil.setIfAbsent(cacheKey, "", Duration.ofMillis(lockTime))) {
            KEY_CACHE.set(cacheKey);
        } else {
            String message = preventRepeatSubmit.message();
            throw new ServiceException(message);
        }
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(preventRepeatSubmit)", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, PreventRepeatSubmit preventRepeatSubmit, Object result) {
        if (result instanceof Response<?> r) {
            try {
                // 成功则不删除 redis 数据，保证在有效时间内无法重复提交
                if (Objects.equals(r.getCode(), ResponseStatusEnum.SUCCESS.getCode())) {
                    return;
                }
                // 接口返回报错信息则删除 redis 数据。可再次请求
                RedisUtil.delete(KEY_CACHE.get());
            } finally {
                KEY_CACHE.remove();
            }
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        StringJoiner params = new StringJoiner(" ");
        if (Objects.isNull(paramsArray)) {
            return params.toString();
        }
        for (Object o : paramsArray) {
            if (Objects.nonNull(o) && !isFilterObject(o)) {
                params.add(JacksonUtil.toJsonStr(o));
            }
        }
        return params.toString();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return MultipartFile.class.isAssignableFrom(clazz.getComponentType());
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.values()) {
                return value instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
