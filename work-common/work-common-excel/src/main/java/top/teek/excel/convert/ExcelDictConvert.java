package top.teek.excel.convert;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import top.teek.excel.annotation.ExcelDictFormat;
import top.teek.excel.dict.DefaultExcelDictHandler;
import top.teek.excel.dict.ExcelDictHandler;
import top.teek.excel.dict.ExcelDictManager;
import top.teek.excel.helper.ExcelHelper;
import top.teek.helper.SpringHelper;
import top.teek.utils.ReflectUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Teeker
 * @date 2024/6/10 17:06:00
 * @note 字典格式化转换处理
 */
public class ExcelDictConvert implements Converter<Object> {

    @Override
    public Class<Object> supportJavaTypeKey() {
        return Object.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return null;
    }

    @Override
    public Object convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        cellData.checkEmpty();

        Field field = contentProperty.getField();
        ExcelDictFormat excelDictFormat = getAnnotation(field);
        String originValue = cellData.getStringValue();

        ExcelDictHandler excelDictHandlerBean = SpringHelper.getBeanIfPresent(excelDictFormat.handler());
        // 先从 Spring 容器获取
        ExcelDictHandler excelDictHandler = Optional.ofNullable(excelDictHandlerBean).orElse(ReflectUtil.newInstance(excelDictFormat.handler()));

        if (!(excelDictHandler instanceof DefaultExcelDictHandler)) {
            String value = ExcelHelper.reverseValueByExp(originValue, excelDictFormat.readExp(), excelDictFormat.mappingKey(), excelDictFormat.separator());
            return Convert.convert(field.getType(), value);
        }

        if (excelDictFormat.cacheHandlerReturn()) {
            // 从缓存取出 value
            ExcelDictManager.Dict dict = ExcelDictManager.get(field);

            if (Objects.nonNull(dict) && Objects.nonNull(dict.getValue())) {
                return Convert.convert(field.getType(), dict.getValue());
            }

            // 如果没有，则读取 excelDictHandler#getValue 返回的值作为 value 缓存
            String value = excelDictHandler.getValue(originValue);

            if (Objects.isNull(dict)) {
                dict = new ExcelDictManager.Dict();
                dict.setValue(value);
                ExcelDictManager.set(field, dict);
            }

            if (Objects.isNull(dict.getValue())) {
                dict.setValue(value);
                ExcelDictManager.set(field, dict);
            }

            return Convert.convert(field.getType(), value);
        }

        return Convert.convert(field.getType(), excelDictHandler.getValue(originValue));

    }

    @Override
    public WriteCellData<String> convertToExcelData(Object value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (ObjectUtil.isNull(value)) {
            return new WriteCellData<>("");
        }

        Field field = contentProperty.getField();
        ExcelDictFormat excelDictFormat = getAnnotation(field);
        String originValue = Convert.toStr(value);
        // 先从 Spring 容器获取
        ExcelDictHandler excelDictHandler = SpringHelper.getBeanIfPresent(excelDictFormat.handler());
        if (Objects.isNull(excelDictHandler)) {
            // Spring 容器获取失败，则手动调用无参构造器创建
            excelDictHandler = ReflectUtil.newInstance(excelDictFormat.handler());
        }

        if (Objects.isNull(excelDictHandler) || excelDictHandler instanceof DefaultExcelDictHandler) {
            String label = ExcelHelper.parseValueByExp(originValue, excelDictFormat.readExp(), excelDictFormat.mappingKey(), excelDictFormat.separator());
            return new WriteCellData<>(label);
        }

        if (excelDictFormat.cacheHandlerReturn()) {
            // 从缓存取出 label
            ExcelDictManager.Dict dict = ExcelDictManager.get(field);

            if (Objects.nonNull(dict) && Objects.nonNull(dict.getLabel())) {
                return new WriteCellData<>(dict.getLabel());
            }

            // 如果没有，则读取 excelDictHandler#getLabel 返回的值作为 label 缓存
            String label = excelDictHandler.getLabel(originValue);

            if (Objects.isNull(dict)) {
                dict = new ExcelDictManager.Dict();
                dict.setLabel(label);
                ExcelDictManager.set(field, dict);
            }

            if (Objects.isNull(dict.getLabel())) {
                dict.setLabel(label);
                ExcelDictManager.set(field, dict);
            }

            return new WriteCellData<>(label);
        }

        return new WriteCellData<>(excelDictHandler.getLabel(originValue));
    }

    private ExcelDictFormat getAnnotation(Field field) {
        return AnnotationUtil.getAnnotation(field, ExcelDictFormat.class);
    }
}
