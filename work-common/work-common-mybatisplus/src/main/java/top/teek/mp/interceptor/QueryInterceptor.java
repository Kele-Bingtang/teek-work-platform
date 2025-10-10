package top.teek.mp.interceptor;

import cn.hutool.core.util.StrUtil;
import top.teek.mp.utils.JSqlParseUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

/**
 * @author Teeker
 * @date 2023/7/4 23:06
 * @note 查询拦截器，做以下几个操作：拦截 `%_` 等特殊字符自动转义
 */
@Slf4j
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class QueryInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 处理查询特殊字符参数转义
        try {
            handleLikeSpecialSymbol(invocation);
        } catch (Exception e) {
            log.error("查询拦截器-处理查询特殊字符参数转义失败", e);
        }
        return invocation.proceed();
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }

    /**
     * 处理 Like 特殊字符
     *
     * @param invocation
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void handleLikeSpecialSymbol(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
        Object[] args = invocation.getArgs();
        if (args == null || args.length < 2) {
            return;
        }
        if (!(args[1] instanceof MapperMethod.ParamMap)) {
            return;
        }
        MappedStatement statement = (MappedStatement) args[0];
        MapperMethod.ParamMap<Object> paramMap = (MapperMethod.ParamMap<Object>) args[1];
        BoundSql boundSql = statement.getBoundSql(paramMap);
        Set<String> likeFields = JSqlParseUtil.getLikeField(boundSql.getSql());
        if (likeFields == null || likeFields.isEmpty()) {
            return;
        }
        for (String likeField : likeFields) {
            String camelLikeField = StrUtil.toCamelCase(likeField);
            String getMethodName = "get" + StrUtil.upperFirst(camelLikeField);
            String setMethodName = "set" + StrUtil.upperFirst(camelLikeField);
            for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
                // mybatis-plus ew 参数解析
                if (parameterMapping.getProperty().startsWith("ew.")) {
                    Object ew = paramMap.get("ew");
                    if (!(ew instanceof AbstractWrapper)) {
                        continue;
                    }
                    AbstractWrapper queryWrapper = (AbstractWrapper) ew;
                    if (parameterMapping.getProperty().equals("ew.entity." + camelLikeField)) {
                        // entity参数处理
                        Method getMethod = ReflectionUtils.findMethod(queryWrapper.getEntity().getClass(), getMethodName);
                        if (getMethod == null) {
                            continue;
                        }
                        Object oldValue = getMethod.invoke(queryWrapper.getEntity());
                        Method setMethod = ReflectionUtils.findMethod(queryWrapper.getEntity().getClass(), setMethodName, String.class);
                        if (setMethod == null) {
                            continue;
                        }
                        setMethod.invoke(queryWrapper.getEntity(), escapeStr(Objects.toString(oldValue, "")));
                    } else if (parameterMapping.getProperty().equals("ew.paramNameValuePairs." + camelLikeField)) {
                        // 值参数处理 TODO
                    }
                }
            }
        }
    }

    public static String escapeStr(String str) {
        if (StringUtil.hasText(str)) {
            str = str.replaceAll("\\\\", "\\\\\\\\");
            str = str.replaceAll("_", "\\\\_");
            str = str.replaceAll("%", "\\\\%");
        }
        return str;
    }
}