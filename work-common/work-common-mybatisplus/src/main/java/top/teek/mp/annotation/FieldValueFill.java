package top.teek.mp.annotation;

import java.lang.annotation.*;

/**
 * @author Teeker
 * @date 2023/11/18 19:33
 * @note
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface FieldValueFill {
    ValueStrategy value() default ValueStrategy.UUID;
}
