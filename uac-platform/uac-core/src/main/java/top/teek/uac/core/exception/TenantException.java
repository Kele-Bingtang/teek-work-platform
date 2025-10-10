package top.teek.uac.core.exception;

import top.teek.core.base.BaseCommonEnum;
import top.teek.core.exception.BaseException;

/**
 * @author Teeker
 * @date 2023/11/13 23:30
 * @note
 */
public class TenantException extends BaseException {
    public TenantException(Integer code) {
        super(code);
    }

    public TenantException(String message) {
        super(message);
    }

    public TenantException(Integer code, String status) {
        super(code, status);
    }

    public TenantException(Integer code, String status, String message) {
        super(code, status, message);
    }

    public TenantException(BaseCommonEnum baseCommonEnum) {
        super(baseCommonEnum);
    }
}
