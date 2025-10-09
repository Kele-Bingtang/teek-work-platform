package cn.youngkbt.uac.core.log.aspect;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.youngkbt.helper.SpringHelper;
import cn.youngkbt.security.utils.SecurityUtils;
import cn.youngkbt.uac.core.log.annotation.OperateLog;
import cn.youngkbt.uac.core.log.enums.BusinessStatus;
import cn.youngkbt.uac.core.log.event.OperaLogEvent;
import cn.youngkbt.utils.AddressUtil;
import cn.youngkbt.utils.JacksonUtil;
import cn.youngkbt.web.utils.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Kele-Bingtang
 * @date 2024/4/27 16:24:41
 * @note
 */
@Slf4j
@Aspect
@Component
public class OperaLogAspect {

    /**
     * 排除敏感属性字段
     */
    protected static final String[] EXCLUDE_PROPERTIES = {"password", "oldPassword", "newPassword", "confirmPassword"};

    /**
     * 计算操作消耗时间
     */
    private static final ThreadLocal<StopWatch> TIME_THREADLOCAL = new ThreadLocal<>();

    @Before("@annotation(operateLog)")
    public void before(JoinPoint joinPoint, OperateLog operateLog) {
        StopWatch stopWatch = new StopWatch();
        TIME_THREADLOCAL.set(stopWatch);
        stopWatch.start();
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(operateLog)", returning = "jsonResult")
    public void afterReturning(JoinPoint joinPoint, OperateLog operateLog, Object jsonResult) {
        handleLog(joinPoint, operateLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(operateLog)", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, OperateLog operateLog, Exception e) {
        handleLog(joinPoint, operateLog, e, null);
    }

    private void handleLog(final JoinPoint joinPoint, OperateLog operateLog, final Exception e, Object jsonResult) {
        try {
            OperaLogEvent operaLogEvent = new OperaLogEvent();
            operaLogEvent.setOperaName(SecurityUtils.getUsername());
            operaLogEvent.setTenantId(SecurityUtils.getTenantId());
            operaLogEvent.setStatus(BusinessStatus.SUCCESS.ordinal());
            operaLogEvent.setOperaTime(LocalDateTime.now());

            // 请求的地址
            String ip = ServletUtil.getClientIp();
            operaLogEvent.setOperaIp(ip);
            operaLogEvent.setOperaLocation(AddressUtil.getRealAddressByIp(ip));

            HttpServletRequest request = ServletUtil.getRequest();
            if (Objects.nonNull(request)) {
                operaLogEvent.setOperaUrl(StringUtils.substring(request.getRequestURI(), 0, 255));
                // 设置请求方式
                operaLogEvent.setRequestMethod(request.getMethod());
            }

            if (e != null) {
                operaLogEvent.setStatus(BusinessStatus.FAIL.ordinal());
                operaLogEvent.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 3800));
            }

            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operaLogEvent.setMethod(className + "." + methodName + "()");

            // 处理设置注解上的参数
            getControllerMethodInfo(joinPoint, operateLog, operaLogEvent, jsonResult);

            // 设置消耗时间
            StopWatch stopWatch = TIME_THREADLOCAL.get();
            stopWatch.stop();
            operaLogEvent.setCostTime(stopWatch.getTime());
            // 发布事件
            SpringHelper.publishEvent(operaLogEvent);

        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息:{}", exp.getMessage());
        } finally {
            TIME_THREADLOCAL.remove();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于 Controller 层注解
     */
    private void getControllerMethodInfo(JoinPoint joinPoint, OperateLog operateLog, OperaLogEvent operaLogEvent, Object jsonResult) {
        // 设置业务类型
        operaLogEvent.setBusinessType(operateLog.businessType().ordinal());
        // 设置标题
        operaLogEvent.setTitle(operateLog.title());
        // 设置操作人类别
        operaLogEvent.setOperatorType(operateLog.operatorType().ordinal());
        // 是否需要保存 request，参数和值
        if (operateLog.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, operaLogEvent, operateLog.excludeParamNames());
        }

        // 是否需要保存 response，参数和值
        if (operateLog.isSaveResponseData() && Objects.nonNull(jsonResult)) {
            operaLogEvent.setJsonResult(StringUtils.substring(JacksonUtil.toJsonStr(jsonResult), 0, 2000));
        }
    }

    /**
     * 获取请求的参数，放到 log 中
     *
     * @param operaLogEvent 操作日志
     */
    private void setRequestValue(JoinPoint joinPoint, OperaLogEvent operaLogEvent, String[] excludeParamNames) {
        // 创建一个键为字符串，值也为字符串的 Map，用于存储请求参数
        Map<String, String> paramsMap = ServletUtil.getParamMap(ServletUtil.getRequest());

        // 获取请求的方法类型（如 GET、POST）
        String requestMethod = operaLogEvent.getRequestMethod();

        // 如果参数 Map 为空，并且请求方法为 PUT 或 POST
        if (MapUtil.isEmpty(paramsMap) && StringUtils.equalsAny(requestMethod, HttpMethod.PUT.name(), HttpMethod.POST.name(), HttpMethod.DELETE.name())) {
            // 将方法参数转换为字符串，限制长度为 2000 字符，并设置到操作日志事件的参数中
            String params = argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            operaLogEvent.setOperaParam(StringUtils.substring(params, 0, 2000));
        } else {
            // 如果不是上述情况，从参数 Map 中移除指定的不需记录的属性
            MapUtil.removeAny(paramsMap, EXCLUDE_PROPERTIES);
            MapUtil.removeAny(paramsMap, excludeParamNames);
            // 将处理后的参数 Map 转换为 JSON 字符串，同样限制长度为 2000 字符，然后设置到操作日志事件的参数中
            operaLogEvent.setOperaParam(StringUtils.substring(JacksonUtil.toJsonStr(paramsMap), 0, 2000));
        }
    }

    /**
     * 参数拼装
     *
     * @param paramsArray 参数数组
     */
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
        StringJoiner params = new StringJoiner(" ");
        if (ArrayUtil.isEmpty(paramsArray)) {
            return params.toString();
        }
        String[] exclude = ArrayUtil.addAll(excludeParamNames, EXCLUDE_PROPERTIES);
        for (Object o : paramsArray) {
            if (Objects.nonNull(o) && !isFilterObject(o)) {
                String str = "";
                if (o instanceof List<?> list) {
                    List<Map<String, Object>> list1 = new ArrayList<>();
                    for (Object obj : list) {
                        String str1 = JacksonUtil.toJsonStr(obj);
                        Map<String, Object> map = JacksonUtil.toJson(str1, Map.class);
                        if (MapUtil.isNotEmpty(map)) {
                            MapUtil.removeAny(map, exclude);
                            list1.add(map);
                        }
                    }
                    str = JacksonUtil.toJsonStr(list1);
                } else {
                    // POST、PUT 请求的 paramsArray 是实体类，因此可以将其转为 Map，然后过滤后转为 String
                    str = JacksonUtil.toJsonStr(o);
                    Map<String, Object> map = JacksonUtil.toJson(str, Map.class);
                    if (MapUtil.isNotEmpty(map)) {
                        MapUtil.removeAny(map, EXCLUDE_PROPERTIES);
                        MapUtil.removeAny(map, excludeParamNames);
                        str = JacksonUtil.toJsonStr(map);
                    }
                }
                params.add(str);
            }
        }
        return params.toString();
    }

    /**
     * 判断是否需要过滤的对象
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();

        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                if (value instanceof MultipartFile) {
                    return true; // 只要发现一个 MultipartFile 实例，就返回 true
                }
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.values()) {
                if (value instanceof MultipartFile) {
                    return true; // 只要发现一个 MultipartFile 实例，就返回 true
                }
            }
        }

        // 移除循环内的直接 return，改为在循环结束后判断
        return o instanceof MultipartFile ||
                o instanceof HttpServletRequest ||
                o instanceof HttpServletResponse ||
                o instanceof BindingResult;
    }
}
