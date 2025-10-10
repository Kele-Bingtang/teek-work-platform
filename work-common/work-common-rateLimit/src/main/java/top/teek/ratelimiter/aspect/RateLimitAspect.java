package top.teek.ratelimiter.aspect;

import cn.hutool.core.text.CharSequenceUtil;
import top.teek.helper.SpElParserHelper;
import top.teek.ratelimiter.annotations.RateLimit;
import top.teek.ratelimiter.enumerate.RateLimitType;
import top.teek.ratelimiter.properties.RateLimitProperties;
import top.teek.redis.constants.RedisConstants;
import top.teek.redis.exception.RedisLimitException;
import top.teek.security.utils.LoginHelper;
import top.teek.utils.StringUtil;
import top.teek.web.utils.ServletUtil;
import top.teek.web.utils.WebUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.scripting.support.ResourceScriptSource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2023/6/30 23:25
 * @note
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class RateLimitAspect {

    private final StringRedisTemplate stringRedisTemplate;
    private final RateLimitProperties rateLimitProperties;

    private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();
    private static final TemplateParserContext TEMPLATE_PARSER_CONTEXT = new TemplateParserContext();

    @Pointcut("execution(* com..controller..*.*(..)) || @annotation(top.teek.ratelimiter.annotations.RateLimit)")
    private void check() {
    }

    private DefaultRedisScript<Long> redisScript;

    @PostConstruct
    public void init() {
        redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("rateLimiter.lua")));
    }

    @Before("check()")
    public void before(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 拿到 RedisLimit 注解，如果存在则说明需要限流
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);
        HttpServletRequest request = WebUtil.getRequest();
        if (Objects.isNull(request)) {
            return;
        }
        // StrUtil.containsAnyIgnoreCase 判断请求的 url 是否有配置文件限流的 urls
        if (Objects.nonNull(rateLimit) || CharSequenceUtil.containsAnyIgnoreCase(request.getRequestURI(), rateLimitProperties.getUrls())) {
            // 获取 redis 的 key
            String key;
            long limit;
            long expire;
            if (Objects.nonNull(rateLimit)) {
                key = getLimitKey(rateLimit.key(), rateLimit.rateLimitType(), joinPoint);
                limit = rateLimit.count();
                expire = rateLimit.expire();
            } else {
                // 根据 URI + 用户 ID 限流
                key = getLimitKey(request.getRequestURI(), RateLimitType.USER, joinPoint);
                limit = rateLimitProperties.getLimit();
                expire = rateLimitProperties.getExpire();
            }
            if (!StringUtil.hasText(key)) {
                throw new RedisLimitException("redis key cannot be null");
            }

            List<String> keys = new ArrayList<>();
            keys.add(key);
            Long count = stringRedisTemplate.execute(redisScript, keys, String.valueOf(limit), String.valueOf(expire));
            log.info("接口限流, 尝试访问次数为 {}，key：{}", count, key);

            if (count == 0) {
                log.debug("接口限流, 导致获取 key 失败，key 为 {}", key);
                throw new RedisLimitException(Objects.nonNull(rateLimit) ? rateLimit.msg() : "请求过于频繁！");
            }
        }
    }

    private String getLimitKey(String key, RateLimitType rateLimitType, JoinPoint joinPoint) {
        StringBuilder stringBuffer = new StringBuilder(RedisConstants.SERVER_REQUEST_LIMIT);

        if (StringUtil.hasText(key)) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            key = SpElParserHelper.parse(signature.getMethod(), joinPoint.getArgs(), key, String.class);
        }

        if (rateLimitType == RateLimitType.USER) {
            // 获取用户 ID
            stringBuffer.append(LoginHelper.getUsername()).append(":");
        } else if (rateLimitType == RateLimitType.IP) {
            // 获取请求 IP
            stringBuffer.append(ServletUtil.getClientIp()).append(":");
        } else if (rateLimitType == RateLimitType.CLUSTER) {
            // 获取客户端实例 ID
            // stringBuffer.append(getClientId()).append(":");
        }

        return stringBuffer.append(key).toString();
    }
}
