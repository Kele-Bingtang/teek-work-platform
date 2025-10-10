package top.teek.encrypt.annotation;

import top.teek.encrypt.enumerate.AlgorithmType;
import top.teek.encrypt.enumerate.EncodeType;

import java.lang.annotation.*;

/**
 * @author Teeker
 * @date 2024/6/8 21:35:52
 * @note 数据库数据加解密注解
 */
@Documented
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptField {
    /**
     * 加密算法
     */
    AlgorithmType algorithm() default AlgorithmType.DEFAULT;

    /**
     * 秘钥。AES、SM4 需要
     */
    String password() default "";

    /**
     * 公钥。RSA、SM2 需要
     */
    String publicKey() default "";

    /**
     * 私钥。RSA、SM2 需要
     */
    String privateKey() default "";

    /**
     * 编码方式。对加密算法为 base64 不起作用
     */
    EncodeType encode() default EncodeType.DEFAULT;
}
