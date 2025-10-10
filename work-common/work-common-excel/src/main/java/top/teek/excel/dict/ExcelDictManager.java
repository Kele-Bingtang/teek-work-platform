package top.teek.excel.dict;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Teeker
 * @date 2024/6/10 21:49:19
 * @note
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelDictManager {
    private static final ThreadLocal<Map<Field, Dict>> FIELD_DICT_CACHE = ThreadLocal.withInitial(ConcurrentHashMap::new);
    
    public static void set(Field field, Dict dict) {
        FIELD_DICT_CACHE.get().put(field, dict);
    }
    
    public static Dict get(Field field) {
        return FIELD_DICT_CACHE.get().get(field);
    }

    public static Map<Field, Dict> getAll() {
        return FIELD_DICT_CACHE.get();
    }
    
    public static void remove() {
        FIELD_DICT_CACHE.remove();
    }
    
    @Data
    public static class Dict {
        private String value;
        private String label;
    }
}
