package top.teek.redis.exception;

import top.teek.core.exception.BaseException;

/**
 * @author Teeker
 * @date 2023/6/30 23:26
 * @note
 */
public class RedisLimitException extends BaseException {
    public RedisLimitException(String msg) {
        super(msg);
    }
}