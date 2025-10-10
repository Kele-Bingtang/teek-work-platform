package top.teek.excel.dict;

/**
 * @author Teeker
 * @date 2024/6/10 20:06:09
 * @note 字典处理接口，搭配 ExcelDictFormat 注解使用
 *
 * <p>
 * 注意：如果自定义了一个类实现 ExcelDictHandler 接口，且 ExcelDictFormat#cacheHandlerReturn 为 false，那么 getLabel、getValue 方法会在导出导出的时候重复调用，Excel 有多少行数据就会调用多少次这两个方法
 * 因此如果涉及「数据库的交互操作」则开启 ExcelDictFormat#cacheHandlerReturn
 * </p>
 */
public interface ExcelDictHandler {

    /**
     * Java 导出为 Excel 时候调用，将返回的值作为 Excel 展示值
     *
     * @param value Java 属性值
     * @return 展示的值
     */
    String getLabel(String value);

    /**
     * Excel导入为 Java 时候调用，将返回的值作为 Java 属性值
     *
     * @param label Excel 值
     * @return 展示的值
     */
    String getValue(String label);
}
