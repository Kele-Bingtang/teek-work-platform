package top.teek.core.exception;


import top.teek.core.base.BaseCommonEnum;

/**
 * @author Teeker
 * @date 2023/6/30 23:16
 * @note
 */
public class ServiceException extends BaseException {

    public ServiceException() {
        super();
    }

    public ServiceException(Integer code) {
        super(code);
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Integer code, String status) {
        super(code, status);
    }

    public ServiceException(Integer code, String status, String message) {
        super(code, status, message);
    }

    public ServiceException(BaseCommonEnum baseCommonEnum) {
        super(baseCommonEnum);
    }
}
