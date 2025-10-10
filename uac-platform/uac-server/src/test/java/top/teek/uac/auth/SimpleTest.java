package top.teek.uac.auth;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

/**
 * @author Teeker
 * @date 2023/11/14 22:11
 * @note
 */
@SpringBootTest
public class SimpleTest {

    void test01() {
        
        List<String> stringList = Arrays.asList("b", "a", "c");
        List<Map<String, Object>> mapList = new ArrayList<>();
        mapList.add(createMap("a", 1));
        mapList.add(createMap("b", 3));
        mapList.add(createMap("c", 2));
        mapList.add(createMap("e", 4));
        mapList.add(createMap("d", 10));
        mapList.add(createMap("f", 7));

        Collections.sort(mapList, (map1, map2) -> {
            for (String str : stringList) {
                if (map1.containsKey(str) && !map2.containsKey(str)) {
                    return -1;
                } else if (!map1.containsKey(str) && map2.containsKey(str)) {
                    return 1;
                }
            }
            return 0;
        });

        System.out.println(mapList);
    }

    private static Map<String, Object> createMap(String str, int num) {
        Map<String, Object> map = new HashMap<>();
        map.put(str, num);
        return map;
    }
}
