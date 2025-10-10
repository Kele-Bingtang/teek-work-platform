package top.teek.cache.manager;

import top.teek.core.date.DatePatternPlus;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import top.teek.redis.config.RedisTemplateConfig;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Teeker
 * @date 2024/6/11 23:14:15
 * @note Caffeine + Redis 双重缓存管理
 */
@RequiredArgsConstructor
public class CaffeineRedisCacheManager implements CacheManager {
    Map<String, Cache> cacheMap = new ConcurrentHashMap<>();

    private final RedisConnectionFactory redisConnectionFactory;
    private final CacheProperties cacheProperties;

    @Override
    public Cache getCache(String cacheName) {
        // 重写 cacheName 支持多参数。如：#cacheName#ttl
        String[] array = StringUtils.delimitedListToStringArray(cacheName, "#");
        cacheName = array[0];

        Cache cache = cacheMap.get(cacheName);
        if (Objects.nonNull(cache)) {
            return cache;
        }

        // Redis 缓存配置项
        RedisCacheConfiguration configuration = createConfiguration(cacheProperties);

        // cacheName 后面的参数默认是 ttl
        if (array.length > 1) {
            long seconds = DurationStyle.detectAndParse(array[1]).toSeconds();
            configuration.entryTtl(Duration.ofSeconds(seconds));
        }

        // 创建 RedisCacheManager，从里面获取 Redis 的缓存类
        RedisCacheManager redisCacheManager = new RedisCacheManager(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory), configuration);

        // 获取 RedisCache，里面只想 Redis 封装了 Redis 的命令操作
        Cache redisCache = redisCacheManager.getCache(cacheName);

        CaffeineRedisCache newCache = new CaffeineRedisCache(redisCache);
        cacheMap.put(cacheName, newCache);
        return newCache;
    }

    /**
     * 参考 Spring 实现 Redis Cache 的配置项：{@link org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration#createConfiguration}
     *
     * @param cacheProperties Spring Cache 缓存配置项
     */
    private RedisCacheConfiguration createConfiguration(CacheProperties cacheProperties) {
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        RedisCacheConfiguration config = RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(getJacksonSerializer()));

        if (Objects.nonNull(redisProperties.getTimeToLive())) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }else {
            // 默认 2 小时
            config = config.entryTtl(Duration.ofHours(2));
        }
        if (Objects.nonNull(redisProperties.getKeyPrefix())) {
            config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }

    /**
     * 获取 Jackson 序列化器，序列化到 Redis 的值，方法参考 {@link RedisTemplateConfig#initRedisTemplate(RedisTemplate)}
     * @return Jackson 序列化器
     */
    private Jackson2JsonRedisSerializer<Object> getJacksonSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

        // 解决 Redis 无法存入 LocalDateTime 等 JDK8 的时间类
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        /*
         * 新增 LocalDateTime 序列化、反序列化规则
         */
        javaTimeModule
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DatePatternPlus.NORM_DATETIME_FORMATTER)) // yyyy-MM-dd HH:mm:ss
                .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME)) // HH:mm:ss
                .addSerializer(Instant.class, InstantSerializer.INSTANCE) // Instant 类型序列化
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DatePatternPlus.NORM_DATETIME_FORMATTER)) // yyyy-MM-dd HH:mm:ss
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE)) // yyyy-MM-dd
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME)) // HH:mm:ss
                .addDeserializer(Instant.class, InstantDeserializer.INSTANT);// Instant 反序列化

        objectMapper.registerModules(javaTimeModule);

        // 使用 Jackson2JsonRedisSerialize 替换默认序列化
        return new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(cacheMap.keySet());
    }
}
