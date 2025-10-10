package top.teek.datasource.processor;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Teeker
 * @date 2023/12/26 21:04
 * @note 方法的最后一个参数作为数据源切换标记
 */
public class LastParamDsProcessor extends DsProcessor {
    private static final String HEADER_PREFIX = "#last";

    @Override
    public boolean matches(String key) {
        if (key.startsWith(HEADER_PREFIX)) {
            DynamicDataSourceContextHolder.clear();
            return true;
        }
        return false;
    }

    @Override
    public String doDetermineDatasource(MethodInvocation methodInvocation, String key) {
        Object[] arguments = methodInvocation.getArguments();
        return String.valueOf(arguments[arguments.length - 1]);
    }
}