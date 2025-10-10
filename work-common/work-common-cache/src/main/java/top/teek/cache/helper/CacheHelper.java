package top.teek.cache.helper;

import top.teek.helper.SpringHelper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Teeker
 * @date 2024/6/11 22:43:34
 * @note 缓存工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheHelper {

    private static final CacheManager CACHE_MANAGER = SpringHelper.getBean(CacheManager.class);

    /**
     * 获取缓存组内所有的 KEY
     *
     * @param cacheNames 缓存组名称
     */
    public static Set<Object> keys(String cacheNames) {
        ConcurrentMap<Object, Object> map = (ConcurrentMap<Object, Object>) CACHE_MANAGER.getCache(cacheNames).getNativeCache();
        return map.keySet();
    }

    /**
     * 获取缓存值
     *
     * @param cacheNames 缓存组名称
     * @param key        缓存 key
     */
    public static <T> T get(String cacheNames, Object key) {
        Cache.ValueWrapper wrapper = CACHE_MANAGER.getCache(cacheNames).get(key);
        return wrapper != null ? (T) wrapper.get() : null;
    }

    /**
     * 保存缓存值
     *
     * @param cacheNames 缓存组名称
     * @param key        缓存 key
     * @param value      缓存值
     */
    public static void put(String cacheNames, Object key, Object value) {
        CACHE_MANAGER.getCache(cacheNames).put(key, value);
    }

    /**
     * 删除缓存值
     *
     * @param cacheNames 缓存组名称
     * @param key        缓存 key
     */
    public static void evict(String cacheNames, Object key) {
        CACHE_MANAGER.getCache(cacheNames).evict(key);
    }

    /**
     * 清空缓存值
     *
     * @param cacheNames 缓存组名称
     */
    public static void clear(String cacheNames) {
        CACHE_MANAGER.getCache(cacheNames).clear();
    }

}
