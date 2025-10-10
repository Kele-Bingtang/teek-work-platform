package top.teek.excel.convert;

import cn.hutool.core.util.ObjectUtil;
import top.teek.utils.JacksonUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.util.Map;

/**
 * @author Teeker
 * @date 2024/6/14 01:25:06
 * @note Java 自定义类转换。使用 Jackson 转为 string 字符串。只支持 Java 转 Excel，不支持 Excel 转 Java
 */
public class ExcelClassConvert implements Converter<Object> {
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
        // 转为 Map，因此实体类使用该 converter 时，属性必须为 Map
        return JacksonUtil.toJson(cellData.getStringValue(), Map.class);
    }

    @Override
    public WriteCellData<String> convertToExcelData(Object value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (ObjectUtil.isNull(value)) {
            return new WriteCellData<>("");
        }

        return new WriteCellData<>(JacksonUtil.toJsonStr(value));
    }
}
