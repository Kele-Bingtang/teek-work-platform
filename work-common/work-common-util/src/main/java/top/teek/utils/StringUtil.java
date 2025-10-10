package top.teek.utils;

import cn.hutool.core.convert.Convert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Teeker
 * @date 2023/11/26 23:19
 * @note
 */
public class StringUtil extends StringUtils {

    public static final String SEPARATOR = ",";

    public static boolean hasText(String... content) {
        for (String s : content) {
            if (!hasText(s)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasEmpty(String content) {
        return !hasText(content);
    }

    public static boolean hasEmpty(String... content) {
        for (String s : content) {
            if (!hasEmpty(s)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasAnyText(String... content) {
        for (String s : content) {
            if (hasText(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasAnyEmpty(String... content) {
        for (String s : content) {
            if (hasEmpty(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 切分字符串(分隔符默认逗号)
     *
     * @param str 被切分的字符串
     * @return 分割后的数据列表
     */
    public static List<String> splitList(String str) {
        return splitTo(str, Convert::toStr);
    }

    /**
     * 切分字符串
     *
     * @param str       被切分的字符串
     * @param separator 分隔符
     * @return 分割后的数据列表
     */
    public static List<String> splitList(String str, String separator) {
        return splitTo(str, separator, Convert::toStr);
    }

    /**
     * 切分字符串自定义转换(分隔符默认逗号)
     *
     * @param str    被切分的字符串
     * @param mapper 自定义转换
     * @return 分割后的数据列表
     */
    public static <T> List<T> splitTo(String str, Function<? super Object, T> mapper) {
        return splitTo(str, SEPARATOR, mapper);
    }

    /**
     * 切分字符串自定义转换
     *
     * @param str       被切分的字符串
     * @param separator 分隔符
     * @param mapper    自定义转换
     * @return 分割后的数据列表
     */
    public static <T> List<T> splitTo(String str, String separator, Function<? super Object, T> mapper) {
        if (!hasText(str)) {
            return new ArrayList<>(0);
        }
        return Arrays.stream(str.split(separator))
                .filter(Objects::nonNull)
                .map(mapper)
                .collect(Collectors.toList());
    }
}
