package top.teek.excel.convert;

import top.teek.utils.ListUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Teeker
 * @date 2024/6/14 00:40:18
 * @note 处理 Java 类型为 List 的数据
 */
public class ExcelListConverter implements Converter<List> {
    @Override
    public Class supportJavaTypeKey() {
        return List.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public List convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        cellData.checkEmpty();
        String originValue = cellData.getStringValue();
        // Excel 带逗号可以转为 List
        return Arrays.stream(originValue.split(",")).map(String::trim).filter(Objects::nonNull).toList();
    }

    @Override
    public WriteCellData<String> convertToExcelData(List list, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (ListUtil.isEmpty(list)) new WriteCellData<>("");
        
        StringJoiner stringJoiner = new StringJoiner(", ");
        list.forEach(value -> {
            if (Objects.nonNull(value)) {
                stringJoiner.add(value.toString());
            }
        });

        return new WriteCellData<>(stringJoiner.toString());
    }
}
