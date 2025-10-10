package top.teek.cache.manager;

import top.teek.helper.SpringHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

/**
 * @author Teeker
 * @date 2024/6/11 22:50:54
 * @note Caffeine + Redis 实现双重缓存
 */
@RequiredArgsConstructor
public class CaffeineRedisCache implements Cache {

    // 从 CacheConfiguration 获取缓存实例
    private static final com.github.benmanes.caffeine.cache.Cache<Object, Object> CAFFEINE = SpringHelper.getBean("caffeine");

    private final Cache cache;

    public String getUniqueKey(Object key) {
        return cache.getName() + ":" + key;
    }

    @Override
    public String getName() {
        return cache.getName();
    }

    @Override
    public Object getNativeCache() {
        return cache.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        // 从 Caffeine 获取缓存，如果缓存不存在，则从 cache 获取，并缓存到 Caffeine
        Object o = CAFFEINE.get(getUniqueKey(key), k -> cache.get(key));
        return (ValueWrapper) o;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        // 从 Caffeine 获取缓存，如果缓存不存在，则从 cache 获取，并缓存到 Caffeine
        Object o = CAFFEINE.get(getUniqueKey(key), k -> cache.get(key, type));
        return (T) o;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object o = CAFFEINE.get(getUniqueKey(key), k -> cache.get(key, valueLoader));
        return (T) o;
    }

    @Override
    public void put(Object key, Object value) {
        // 在 Caffeine 删除缓存，不能存储 value，否则取出来类型转换报错
        CAFFEINE.invalidate(getUniqueKey(key));
        cache.put(key, value);
    }

    @Override
    public void evict(Object key) {
        // 在 cache 删除缓存
        boolean b = cache.evictIfPresent(key);
        if (b) {
            // 在 Caffeine 删除缓存
            CAFFEINE.invalidate(getUniqueKey(key));
        }
    }

    @Override
    public void clear() {
        cache.clear();
        CAFFEINE.invalidateAll();
    }
}
