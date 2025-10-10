package top.teek.encrypt.enumerate;

/**
 * @author Teeker
 * @date 2024/6/8 22:15:34
 * @note 编码类型
 */
public enum EncodeType {
    /**
     * 默认使用 yml 配置
     */
    DEFAULT,

    /**
     * base64 编码
     */
    BASE64,

    /**
     * 16 进制编码
     */
    HEX;
}
