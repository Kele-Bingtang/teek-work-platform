package top.teek.excel.annotation;

import top.teek.excel.dict.DefaultExcelDictHandler;
import top.teek.excel.dict.ExcelDictHandler;

import java.lang.annotation.*;

/**
 * @author Teeker
 * @date 2024/6/10 17:08:44
 * @note 字典格式化
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelDictFormat {

    /**
     * 自定义实现字典数据处理类
     */
    Class<? extends ExcelDictHandler> handler() default DefaultExcelDictHandler.class;

    /**
     * 是否缓存 自定义实现字典数据处理类 返回的数据（如果处理类涉及数据库操作，建议开启 true）
     */
    boolean cacheHandlerReturn() default true;

    /**
     * 读取内容转表达式 (如: 0:男, 1:女, 2:未知)
     */
    String readExp() default "";

    /**
     * key 和 value 的映射符
     */
    String mappingKey() default ":";

    /**
     * 分隔符，读取字符串组内容，可以加空格，如 Y:是, N:否
     */
    String separator() default ",";

}
