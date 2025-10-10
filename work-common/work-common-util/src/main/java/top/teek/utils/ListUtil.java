package top.teek.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author Teeker
 * @date 2023/12/11 23:50
 * @note
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ListUtil {

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T> boolean isNotEmpty(List<T> list) {
        return !isEmpty(list);
    }

    public static <T> List<T> newArrayList() {
        return new ArrayList<>();
    }

    public static <T> List<T> newArrayList(T... objects) {
        return Arrays.asList(objects);
    }

    public static <T> List<T> newArrayList(List<T> useList, Function<T, T> function) {
        List<T> list = newArrayList();
        useList.forEach(l -> {
            T apply = function.apply(l);
            if (Objects.nonNull(apply)) {
                list.add(apply);
            }
        });
        return list;
    }

    public static <T, V> List<V> newArrayList(List<T> useList, Function<T, V> function, Class<V> clazz) {
        List<V> list = newArrayList();
        useList.forEach(l -> list.add(function.apply(l)));
        return list;
    }
}
