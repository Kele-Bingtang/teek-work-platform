package top.teek.ratelimiter.enumerate;

/**
 * @author Teeker
 * @date 2024/6/11 00:35:47
 * @note
 */
public enum RateLimitType {
    /**
     * 默认策略全局限流
     */
    DEFAULT,

    /**
     * 根据用户 ID 限流
     */
    USER,

    /**
     * 根据请求者 IP 进行限流
     */
    IP,

    /**
     * 实例限流(集群多后端实例)
     */
    CLUSTER
}
