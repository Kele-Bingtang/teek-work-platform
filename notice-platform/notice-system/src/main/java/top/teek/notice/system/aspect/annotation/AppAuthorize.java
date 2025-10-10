package top.teek.notice.system.aspect.annotation;

import java.lang.annotation.*;

/**
 * @author Teeker
 * @date 2024/8/23 00:34:33
 * @note
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AppAuthorize {
    /**
     * SpEl 表达式，AppId
     */
    String value() default "";
}
