package top.teek.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author Teeker
 * @date 2023/9/24 3:02
 * @note
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IdsUtil {
    public static String UUID() {
        return UUID.randomUUID().toString();
    }
    
    public static String simpleUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
