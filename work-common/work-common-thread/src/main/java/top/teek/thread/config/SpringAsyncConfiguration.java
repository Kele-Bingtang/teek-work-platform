package top.teek.thread.config;

import cn.hutool.core.util.ArrayUtil;
import top.teek.core.exception.ServiceException;
import top.teek.helper.SpringHelper;
import top.teek.thread.constants.ThreadConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Arrays;
import java.util.concurrent.Executor;

/**
 * @author Teeker
 * @date 2023/11/16 23:50
 * @note
 */
@EnableAsync(proxyTargetClass = true)
@AutoConfiguration
@Slf4j
public class SpringAsyncConfiguration implements AsyncConfigurer {
    /**
     * 自定义 @Async 注解使用系统线程池
     */
    @Override
    public Executor getAsyncExecutor() {
        return SpringHelper.getBean(ThreadConstant.THREAD_POOL_TASK_EXECUTOR_BEAN_NAME);
    }

    /**
     * 执行异步异常处理
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            log.error("Async exception message - {}", throwable.getMessage());
            StringBuilder sb = new StringBuilder();
            sb.append("Exception message - ")
                    .append(throwable.getMessage())
                    .append(", Method name - ")
                    .append(method.getName());
            if (ArrayUtil.isNotEmpty(objects)) {
                sb.append(", Parameter value - ")
                        .append(Arrays.toString(objects));
            }
            throw new ServiceException(sb.toString());
        };
    }
}
