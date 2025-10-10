package top.teek.integrate.system.helper;

import top.teek.utils.JacksonUtil;
import top.teek.utils.StringUtil;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author Teeker
 * @date 2024/10/28 00:33:55
 * @note
 */
public class IntegrateUtil {
    public static Map<String, Object> getReqParamMap(String sourceMapString, Map<String, Object> targetMap) {
        List<Map<String, String>> reqParamList = new ArrayList<>();
        if (StringUtil.hasText(sourceMapString)) {
            reqParamList = JacksonUtil.toListMap(sourceMapString);
        }
        Map<String, Object> sourceMap = readListMapToMap(reqParamList);
        if (Objects.nonNull(targetMap)) {
            targetMap.forEach((k, v) -> sourceMap.merge(k, v, (v1, v2) ->v2));
        } return sourceMap;
    }

    public static Map<String, Object> readListMapToMap(List<Map<String, String>> list) {
        Map<String, Object> result = new LinkedHashMap<>(list.size());
        list.forEach(map -> {
            String type = map.getOrDefault("type", "");
            String name = map.getOrDefault("name", "");
            Object value = map.getOrDefault("value", "");
            if ("normal".equals(type)) {
                result.put(name, value);
            } else if ("dynamic".equals(type)) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate now = LocalDate.now();
                // 暂时以天为默认值，后面添加属性来判断 
                LocalDate targetLocalDate = now.plus(Long.parseLong(String.valueOf(value)), ChronoUnit.DAYS);
                String targetDate = targetLocalDate.format(dateTimeFormatter);
                result.put(name, targetDate);
            }
        });
        return result;
    }

    /**
     * 获取 URL ? 后面的参数 * * @param reqParamMap 认证参数 * @return GET 请求的参数
     */
    public static String getRequestParams(Map<String, Object> reqParamMap, String sourceUrl) {
        StringBuilder params;
        if (sourceUrl.contains("?")) {
            params = new StringBuilder("&");
        } else {
            params = new StringBuilder("?");
        }
        reqParamMap.forEach((key, value) -> params.append(key).append("=").append(value).append("&"));
        params.deleteCharAt(params.length() - 1);
        return params.toString();
    }

    /**
     * 类型转换：LinkedHashMap 转成 MultiValueMap * * @param map LinkedHashMap 对象 * @return MultiValueMap 对象
     */
    public static MultiValueMap<String, Object> convertMultiValueMap(Map<String, Object> map) {
        if (Objects.isNull(map) || map.isEmpty()) {
            return new LinkedMultiValueMap<>();
        }
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        map.forEach((key, value) -> {
            if (value instanceof List) {
                multiValueMap.put(key, new ArrayList<>(Collections.singletonList(JacksonUtil.toJsonStr(value))));
            } else {
                multiValueMap.put(key, new ArrayList<>(Collections.singletonList(value)));
            }
        });
        return multiValueMap;
    }

    /**
     * 假设 "nest": "data.row"，则从 data 里读取 nest 的内容，即 data.get("data").get("row") * * @param data 数据 * @param nest 读取的层级 * @return 读取 nest 后的数据
     */
    public static Object nestData(Map<String, Object> data, String nest) {
        Map<String, Object> map = new HashMap<>(data);
        String[] nestKeys = nest.trim().split("\\.");
        // nest 为空则不需要处理，直接返回
        if (StringUtil.hasEmpty(nestKeys[0])) {
            return map;
        }
        Object value = null;
        for (String nestKey : nestKeys) {
            value = map.get(nestKey);
            // 读取数据后给 map 赋值，以便下次 for 循环从读取后的内容继续读取
            if (value instanceof String) {
                map = JacksonUtil.toJson((String) value, Map.class);
            } else if (value instanceof Map) {
                map = (Map) value;
            }
        }
        return value;
    }

    /**
     * 假设 "merge": "data.item"，则将 value 在外面包一层 data.item，即如果想读取 value，则 data.item.value 才能读取 * * @param value 数据 * @param merge 合并的数据 * @return 包层后的 value
     */
    public static Object mergeData(Object value, String merge) {
        // 2 的 n 次方
        Map<String, Object> extraAppInfo = new HashMap<>(4);
        String[] mergeKeys = merge.trim().split("\\.");
        // 如果不需要在外面包层，则直接返回 value 
        if (StringUtil.hasEmpty(mergeKeys[0])) {
            return value;
        }
        // 从后面开始包层，如 "merge": "data.item"，则先包 item，再包 data
        for (int i = mergeKeys.length - 1; i >= 0; i--) {
            String mergeKey = mergeKeys[i];
            // 如果是最后一次，则直接包 
            if (i == mergeKeys.length - 1) {
                extraAppInfo.put(mergeKey, value);
            } else {
                // 需要先读取之前包过的数据，再包 
                HashMap<String, Object> tempMap = new HashMap<>();
                tempMap.put(mergeKey, new HashMap<>(extraAppInfo));
                extraAppInfo = new HashMap<>(tempMap);
            }
        }
        return extraAppInfo;
    }

    /**
     * 合并两个 Map，如果 Value 还是 Map，则继续合并 * 如 map1 是 {"data": {"row": "xxx"}}，map2 是 {"data": {"item": "xxx"}}。即 map1 要读取 xxx 为 map.get("data").get("row")，map2 要读取 xxx 为 map2.get("data").get("item") * 则最终合并成 {"data": {"row": "xxx"}, {"item": "xxx"}} * * @param sacrificeMap 需要合并的 map * @param mergeMap 最终合并成功的 map * @return 合并后的数据
     */
    public static Map<String, Object> mergeAndrecursionMap(Map<String, Object> sacrificeMap, Map<String, Object> mergeMap) {
        Map<String, Object> mergeResult = new HashMap<>(mergeMap);
        sacrificeMap.forEach((k, v) -> mergeResult.merge(k, v, (v1, v2) ->
        {
            // 如果 map 的 value 还是 map，则继续合并 
            if (v1 instanceof Map && v2 instanceof Map) {
                return mergeAndrecursionMap((Map<String, Object>) v1, (Map<String, Object>) v2);
            }
            return v2;
        }));
        return mergeResult;
    }

    public static boolean isListString(String data) {
        return data.startsWith("[{");
    }

    public static boolean isMapString(String data) {
        return data.startsWith("{");
    }

    public static String getUrl(String patternUrl, Map<String, Object> patternUrlMap) {
        if (Objects.isNull(patternUrlMap) || patternUrlMap.isEmpty() || !patternUrl.contains("${")) {
            return patternUrl;
        }
        final String[] url = {patternUrl};
        patternUrlMap.forEach((k, v) -> url[0] = url[0].replace("${" + k + "}", String.valueOf(v)));
        return url[0];
    }
}