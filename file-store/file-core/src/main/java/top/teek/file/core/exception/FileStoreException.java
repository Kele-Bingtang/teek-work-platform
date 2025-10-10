package top.teek.file.core.exception;

import top.teek.core.base.BaseCommonEnum;
import top.teek.core.exception.BaseException;

/**
 * @author Teeker
 * @date 2024/8/5 00:50:14
 * @note
 */
public class FileStoreException extends BaseException {
    public FileStoreException(Integer code) {
        super(code);
    }

    public FileStoreException(String message) {
        super(message);
    }

    public FileStoreException(Integer code, String status) {
        super(code, status);
    }

    public FileStoreException(Integer code, String status, String message) {
        super(code, status, message);
    }

    public FileStoreException(BaseCommonEnum baseCommonEnum) {
        super(baseCommonEnum);
    }
}
