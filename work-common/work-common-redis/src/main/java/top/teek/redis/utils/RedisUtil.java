package top.teek.redis.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Teeker
 * @date 2024/1/21 21:47
 * @note
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisUtil {

    private static RedisTemplate<String, Object> redisTemplate;

    public static void init(RedisTemplate<String, Object> redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    /**
     * 获取 keys
     */
    public static Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 判断 key 是否存在
     *
     * @param key 键
     * @return 是否存在 key
     */
    public static boolean hasKey(String key) {
        Boolean hasKey = redisTemplate.hasKey(key);
        if (Objects.nonNull(hasKey)) {
            return hasKey;
        }
        return false;
    }

    /**
     * 获取 key 的过期时间
     *
     * @param key 键
     * @return 剩余过期时间
     */
    public static Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 设置 key 的过期时间
     *
     * @param key  键
     * @param date 过期时间
     * @return 是否设置成功
     */
    public static boolean expireAt(String key, Date date) {
        Boolean result = redisTemplate.expireAt(key, date);
        if (Objects.nonNull(result)) {
            return result;
        }
        return false;
    }

    /**
     * 获取 key 的值
     *
     * @param key 键
     * @return 值
     */
    public static Object getForValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 如果不存在则设置 并返回 true 如果存在则返回 false
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     * @return set成功或失败
     */
    public static <T> boolean setIfAbsent(String key, T value, long timeout, TimeUnit unit) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
        if (Objects.nonNull(result)) {
            return result;
        }
        return false;
    }

    /**
     * 如果不存在则设置 并返回 true 如果存在则返回 false
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     * @return set成功或失败
     */
    public static <T> boolean setIfAbsent(String key, T value, Duration duration) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, duration);
        if (Objects.nonNull(result)) {
            return result;
        }
        return false;
    }

    /**
     * 设置 key 的值
     *
     * @param key   键
     * @param value 值
     */
    public static void setForValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置 key 的值
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间
     */
    public static void setForValue(String key, Object value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    /**
     * 删除 key
     *
     * @param key 键
     * @return 是否删除成功
     */
    public static boolean delete(String key) {
        Boolean result = redisTemplate.delete(key);
        if (Objects.nonNull(result)) {
            return result;
        }
        return false;
    }

    /**
     * 订阅通道接收消息
     *
     * @param channelKey 通道 key
     * @param listener   监听器
     */
    public static String subscribe(String channelKey, MessageListener listener) {
        return redisTemplate.execute((RedisCallback<String>) (connection) -> {
            connection.subscribe(listener, channelKey.getBytes());
            return channelKey;
        });
    }

    /**
     * 发布通道消息
     *
     * @param channelKey 通道key
     * @param message    发送数据
     */
    public static Long publish(String channelKey, Object message) {
        return redisTemplate.convertAndSend(channelKey, message);
    }

}
