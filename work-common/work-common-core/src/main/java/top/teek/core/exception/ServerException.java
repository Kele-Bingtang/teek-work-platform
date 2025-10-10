package top.teek.core.exception;


import top.teek.core.base.BaseCommonEnum;

/**
 * @author Teeker
 * @date 2023/6/30 23:15
 * @note
 */
public class ServerException extends BaseException{

    public ServerException() {
        super();
    }
    
    public ServerException(Integer code) {
        super(code);
    }

    public ServerException(String message) {
        super(message);
    }

    public ServerException(Integer code, String status) {
        super(code, status);
    }

    public ServerException(Integer code, String status, String message) {
        super(code, status, message);
    }

    public ServerException(BaseCommonEnum baseCommonEnum) {
        super(baseCommonEnum);
    }
}
