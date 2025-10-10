package top.teek.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2023/6/30 23:04
 * @note
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class JacksonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 初始化 objectMapper 属性
     * 通过这样的方式，拷贝一份 Spring 创建的 ObjectMapper Bean，不会影响原来的 Bean 配置
     *
     * @param objectMapper ObjectMapper 对象
     * @see JacksonAutoConfiguration
     */
    public static void init(ObjectMapper objectMapper) {
        JacksonUtil.objectMapper = objectMapper;
    }

    /**
     * 对象转为 JSON 字符串
     *
     * @return string
     */
    public static String toJsonStr(Object json) {
        try {
            return objectMapper.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            log.error("Jackson 工具类转换对象为 String 失败", e);
        }
        return null;
    }

    /**
     * JSON 字符串转为 Bean
     * @return T
     */
    public static <T> T toJson(String jsonStr, Class<T> clz) {
        try {
            return objectMapper.readValue(jsonStr, clz);
        } catch (JsonProcessingException e) {
            log.error("Jackson 工具类字符串转换为对象失败", e);
        }
        return null;
    }

    /**
     * JSON 字符串转为 Bean
     * @return T
     */
    public static <T> T toJson(String text, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(text, typeReference);
        } catch (IOException e) {
            log.error("Jackson 解析 JSON 失败，JSON：{}", text, e);
        }
        return null;
    }

    /**
     * JSON 字符串转为 List<Map>
     * @return T
     */
    public static <T> List<T> toListMap(String jsonStr) {
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Map.class);
        try {
            return objectMapper.readValue(jsonStr, listType);
        } catch (JsonProcessingException e) {
            log.error("Jackson 工具类字符串转换为 List 失败", e);
        }
        return null;
    }

    /**
     * JSON 字符串转为 Bean 集合
     * @return T
     */
    public static <T> List<T> toBeanList(String jsonStr, Class<T> clz) {
        try {
            return objectMapper.readValue(jsonStr, new TypeReference<List<T>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Jackson 工具类字符串转换为 List 失败", e);
        }
        return null;
    }

    /**
     * Bean 转换
     * @return T
     */
    public static <T> T toBean(Object object, Class<T> clz) {
        if (Objects.isNull(object)) {
            return null;
        }
        return objectMapper.convertValue(object, clz);
    }

    /**
     * Bean 转换
     * @return T
     */
    public static <T> T toType(Object object, TypeReference<T> typeReference) {
        if (Objects.isNull(object)) {
            return null;
        }
        return objectMapper.convertValue(object, typeReference);
    }

    /**
     * Bean 转换 List<Bean>
     * @return T
     */
    public static <T> List<T> toBeanList(Object object, Class<T> clz) {
        if (Objects.isNull(object)) {
            return null;
        }
        return objectMapper.convertValue(object, new TypeReference<List<T>>() {
        });
    }

    /**
     * 判断是否可以序列化
     *
     * @param value 对象
     * @return 是否可以序列化
     */
    public static boolean canSerialize(@Nullable Object value) {
        if (value == null) {
            return true;
        }
        return objectMapper.canSerialize(value.getClass());
    }
}