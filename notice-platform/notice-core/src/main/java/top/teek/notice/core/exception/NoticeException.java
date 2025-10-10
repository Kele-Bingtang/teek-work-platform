package top.teek.notice.core.exception;

import top.teek.core.base.BaseCommonEnum;
import top.teek.core.exception.BaseException;

/**
 * @author Teeker
 * @date 2024/8/5 01:07:23
 * @note
 */
public class NoticeException extends BaseException {
    public NoticeException(Integer code) {
        super(code);
    }

    public NoticeException(String message) {
        super(message);
    }

    public NoticeException(Integer code, String status) {
        super(code, status);
    }

    public NoticeException(Integer code, String status, String message) {
        super(code, status, message);
    }

    public NoticeException(BaseCommonEnum baseCommonEnum) {
        super(baseCommonEnum);
    }
}
