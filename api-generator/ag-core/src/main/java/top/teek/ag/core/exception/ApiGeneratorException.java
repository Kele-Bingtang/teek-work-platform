package top.teek.ag.core.exception;

import top.teek.core.base.BaseCommonEnum;
import top.teek.core.exception.BaseException;

/**
 * @author Teeker
 * @date 2024/6/19 22:28:38
 * @note
 */
public class ApiGeneratorException extends BaseException {
    public ApiGeneratorException(Integer code) {
        super(code);
    }

    public ApiGeneratorException(String message) {
        super(message);
    }

    public ApiGeneratorException(Integer code, String status) {
        super(code, status);
    }

    public ApiGeneratorException(Integer code, String status, String message) {
        super(code, status, message);
    }

    public ApiGeneratorException(BaseCommonEnum baseCommonEnum) {
        super(baseCommonEnum);
    }
}
