package top.teek.file.system.helper;

import top.teek.core.http.ResponseStatusEnum;
import top.teek.file.core.exception.FileStoreException;
import top.teek.utils.StringUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * @author Teeker
 * @date 2024/8/11 16:52:56
 * @note
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Assert {

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new FileStoreException(ResponseStatusEnum.ERROR.getCode(), ResponseStatusEnum.ERROR.getStatus(), message);
        }
    }

    public static void isTrue(boolean expression, ResponseStatusEnum responseStatusEnum) {
        if (!expression) {
            throw new FileStoreException(responseStatusEnum);
        }
    }

    public static void isNull(@Nullable Object object, String message) {
        if (object != null) {
            throw new FileStoreException(ResponseStatusEnum.ERROR.getCode(), ResponseStatusEnum.ERROR.getStatus(), message);
        }
    }

    public static void isNull(@Nullable Object object, ResponseStatusEnum responseStatusEnum) {
        if (object != null) {
            throw new FileStoreException(responseStatusEnum);
        }
    }

    public static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new FileStoreException(ResponseStatusEnum.ERROR.getCode(), ResponseStatusEnum.ERROR.getStatus(), message);
        }
    }

    public static void notNull(@Nullable Object object, ResponseStatusEnum responseStatusEnum) {
        if (object == null) {
            throw new FileStoreException(responseStatusEnum);
        }
    }

    public static void hasText(String text, String message) {
        if (StringUtil.hasEmpty(text)) {
            throw new FileStoreException(ResponseStatusEnum.ERROR.getCode(), ResponseStatusEnum.ERROR.getStatus(), message);
        }
    }

    public static void hasText(String text, ResponseStatusEnum responseStatusEnum) {
        if (StringUtil.hasEmpty(text)) {
            throw new FileStoreException(responseStatusEnum);
        }
    }

    public static void hasEmpty(String text, String message) {
        if (StringUtil.hasText(text)) {
            throw new FileStoreException(ResponseStatusEnum.ERROR.getCode(), ResponseStatusEnum.ERROR.getStatus(), message);
        }
    }

    public static void hasEmpty(String text, ResponseStatusEnum responseStatusEnum) {
        if (StringUtil.hasText(text)) {
            throw new FileStoreException(responseStatusEnum);
        }
    }
}
