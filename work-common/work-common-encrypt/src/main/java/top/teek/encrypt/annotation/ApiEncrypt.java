package top.teek.encrypt.annotation;

import java.lang.annotation.*;

/**
 * @author Teeker
 * @date 2024/6/8 23:31:30
 * @note 接口返回数据加密
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiEncrypt {
    /**
     * 请求体数据是否进行解密，默认不解密，为 true 时解密
     */
    boolean request() default false;

    /**
     * 响应数据是否加密后返回，默认加密，为 true 时加密
     */
    boolean response() default true;
}
