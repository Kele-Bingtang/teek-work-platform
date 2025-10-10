package top.teek.datasource.meta;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Teeker
 * @date 2024/6/30 17:54:04
 * @note
 */
@Data
public class Column implements Serializable {
    /**
     * 数据源 ID
     */
    private String dataSourceId;
    /**
     * 库
     */
    private String catalog;
    /**
     * 模式
     */
    private String schema;
    /**
     * 表名称
     */
    private String tableName;
    /**
     * 字段名称
     */
    private String columnName;
    /**
     * 字段注释
     */
    private String remarks;
    /**
     * 字段类型
     */
    private String columnType;
    /**
     * 字段类型（经过处理 columnType 得到）
     */
    private String columnProcessType;
    /**
     * 字段长度
     */
    private String columnSize;
    /**
     * 字段精字段度
     */
    private String decimalDigits;
    /**
     * 字段默认值
     */
    private String columnDefault;

    @Serial
    private static final long serialVersionUID = 1L;
}
