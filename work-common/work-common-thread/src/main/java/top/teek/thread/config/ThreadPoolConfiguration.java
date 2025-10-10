package top.teek.thread.config;

import top.teek.thread.constants.ThreadConstant;
import top.teek.thread.properties.ThreadPoolProperties;
import top.teek.thread.utils.ThreadUtil;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Teeker
 * @date 2023/11/16 23:29
 * @note
 */
@AutoConfiguration
@Slf4j
@EnableConfigurationProperties(ThreadPoolProperties.class)
public class ThreadPoolConfiguration {
    /**
     * 核心线程数 = cpu 核心数 + 1
     */
    private final int core = Runtime.getRuntime().availableProcessors() + 1;

    private ScheduledExecutorService scheduledExecutorService;

    @Bean(ThreadConstant.THREAD_POOL_TASK_EXECUTOR_BEAN_NAME)
    @ConditionalOnProperty(prefix = ThreadConstant.THREAD_POOL_NAME_PREFIX, name = "enabled", havingValue = "true")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(ThreadPoolProperties threadPoolProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(Optional.ofNullable(threadPoolProperties.getCorePoolSize()).orElse(core));
        executor.setMaxPoolSize(Optional.ofNullable(threadPoolProperties.getMaxPoolSize()).orElse(core * 2));
        executor.setQueueCapacity(Optional.ofNullable(threadPoolProperties.getQueueCapacity()).orElse(500));
        executor.setKeepAliveSeconds(Optional.ofNullable(threadPoolProperties.getKeepAliveSeconds()).orElse(60));
        executor.setAwaitTerminationSeconds(Optional.ofNullable(threadPoolProperties.getAwaitTerminationSeconds()).orElse(60));
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        
        return executor;
    }

    /**
     * 执行周期性或定时任务
     */
    @Bean(ThreadConstant.SCHEDULED_EXECUTOR_SERVICE_BEAN_NAME)
    protected ScheduledExecutorService scheduledExecutorService() {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(core,
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build(),
                new ThreadPoolExecutor.CallerRunsPolicy()) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                ThreadUtil.printException(r, t);
            }
        };
        this.scheduledExecutorService = scheduledThreadPoolExecutor;
        return scheduledThreadPoolExecutor;
    }

    /**
     * 销毁事件
     */
    @PreDestroy
    public void destroy() {
        try {
            log.info("==== 关闭后台任务任务线程池 ====");
            ThreadUtil.shutdownAndAwaitTermination(scheduledExecutorService);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
