package top.teek.excel.annotation;

import java.lang.annotation.*;

/**
 * @author Teeker
 * @date 2024/6/10 17:42:18
 * @note 枚举格式化（指定枚举类）
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelEnumFormat {

    /**
     * 字典枚举类型
     */
    Class<? extends Enum<?>> enumClass();

    /**
     * 字典枚举类 Value
     */
    String valueField() default "value";

    /**
     * 字典枚举类 label
     */
    String labelField() default "label";
}
