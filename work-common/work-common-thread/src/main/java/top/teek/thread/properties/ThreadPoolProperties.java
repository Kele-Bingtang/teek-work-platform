package top.teek.thread.properties;

import top.teek.thread.constants.ThreadConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Teeker
 * @date 2023/11/16 23:30
 * @note
 */
@Data
@ConfigurationProperties(prefix = ThreadConstant.THREAD_POOL_NAME_PREFIX)
public class ThreadPoolProperties {
    /**
     * 是否开启线程池
     */
    private boolean enabled;
    /**
     * 核心线程数
     */
    private Integer corePoolSize;
    /**
     * 最大线程数
     */
    private Integer maxPoolSize;

    /**
     * 队列最大长度
     */
    private Integer queueCapacity;

    /**
     * 线程池维护线程所允许的空闲时间
     */
    private Integer keepAliveSeconds;
    /**
     * 线程等待连接时间
     */
    private Integer awaitTerminationSeconds;
}
