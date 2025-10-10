package top.teek.datasource.enums;

import top.teek.datasource.exception.DataSourceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Teeker
 * @date 2024/6/30 23:44:30
 * @note 支持的数据源类型枚举，type 一定是小写
 */
@Getter
@AllArgsConstructor
public enum DataSourceType {

    MYSQL("mysql", "com.mysql.cj.jdbc.Driver"),
    ORACLE("oracle", "oracle.jdbc.OracleDriver"),
    POSTGRESQL("postgresql", "org.postgresql.Driver"),
    SQLSERVER("sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    ;

    private final String type;
    private final String driverClass;

    public static String getDriverClass(String type) {
        for (DataSourceType dataSourceType : DataSourceType.values()) {
            if (dataSourceType.getType().equals(type)) {
                return dataSourceType.driverClass;
            }
        }
        throw new DataSourceException("暂不支持 " + type + " 数据源类型");
    }
}
