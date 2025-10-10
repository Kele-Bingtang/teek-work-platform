package top.teek.excel.annotation;

import java.lang.annotation.*;

/**
 * @author Teeker
 * @date 2024/6/10 19:01:05
 * @note Excel 列单元格合并(合并列相同项)
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CellMerge {

    /**
     * col index
     */
    int index() default -1;

    /**
     * 合并需要依赖的其他字段名称
     */
    String[] mergeBy() default {};

}
