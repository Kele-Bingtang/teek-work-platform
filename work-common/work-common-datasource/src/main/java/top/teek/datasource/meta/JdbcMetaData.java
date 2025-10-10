package top.teek.datasource.meta;

import top.teek.datasource.exception.MetaException;
import top.teek.datasource.helper.DataSourceHelper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Teeker
 * @date 2024/6/30 17:59:13
 * @note
 */
public class JdbcMetaData implements MetaData {

    @Override
    public List<Schema> getCatalogs(String dataSourceId) {
        DataSource dataSource = DataSourceHelper.getDataSource(dataSourceId);

        try (Connection connection = dataSource.getConnection()) {
            ResultSet resultSet = connection.getMetaData().getCatalogs();

            List<Schema> catalogList = new ArrayList<>();
            while (resultSet.next()) {
                String schemaName = resultSet.getString(1);
                // 忽略系统 schema
                if ("information_schema".equalsIgnoreCase(schemaName) || "performance_schema".equalsIgnoreCase(schemaName) || "pg_catalog".equals(schemaName)) {
                    continue;
                }
                Schema schema = new Schema();
                schema.setDataSourceId(dataSourceId);
                schema.setSchema(schemaName);
                schema.setCatalog(schemaName);
                catalogList.add(schema);
            }
            return catalogList;
        } catch (SQLException e) {
            throw new MetaException("获取数据源连接失败：" + e);
        }
    }

    @Override
    public List<Schema> getSchemas(String dataSourceId, String catalog) {
        DataSource dataSource = DataSourceHelper.getDataSource(dataSourceId);

        try (Connection connection = dataSource.getConnection()) {
            ResultSet resultSet = connection.getMetaData().getSchemas();

            List<Schema> schemaList = new ArrayList<>();
            while (resultSet.next()) {
                String schemaName = resultSet.getString(1);
                // 忽略系统 schema
                if ("information_schema".equalsIgnoreCase(schemaName) || "performance_schema".equalsIgnoreCase(schemaName) || "pg_catalog".equals(schemaName)) {
                    continue;
                }
                Schema schema = new Schema();
                schema.setDataSourceId(dataSourceId);
                schema.setSchema(schemaName);
                schema.setCatalog(schemaName);
                schemaList.add(schema);
            }
            return schemaList;
        } catch (SQLException e) {
            throw new MetaException("获取数据源连接失败：" + e);
        }
    }

    @Override
    public List<Table> getTables(String dataSourceId, String catalog, String schema) {
        DataSource dataSource = DataSourceHelper.getDataSource(dataSourceId);

        try (Connection connection = dataSource.getConnection()) {
            String[] tableType = {"TABLE", "VIEW"};

            ResultSet resultSet = connection.getMetaData().getTables(catalog, schema, null, tableType);

            List<Table> tableList = new ArrayList<>();

            // 获取表和视图
            while (resultSet.next()) {
                Table table = new Table();
                table.setTableName(resultSet.getString("TABLE_NAME"));
                table.setRemarks(resultSet.getString("REMARKS"));
                table.setTableType(resultSet.getString("TABLE_TYPE"));
                table.setCatalog(resultSet.getString("TABLE_CAT"));
                table.setDataSourceId(dataSourceId);
                table.setSchema(schema);
                tableList.add(table);
            }

            // 获取存储过程
            ResultSet procedures = connection.getMetaData().getProcedures(catalog, schema, null);
            while (procedures.next()) {
                Table table = new Table();
                table.setTableName(procedures.getString("PROCEDURE_NAME"));
                table.setRemarks(procedures.getString("REMARKS"));
                table.setTableType("PROCEDURE");
                table.setCatalog(procedures.getString("PROCEDURE_CAT"));
                table.setDataSourceId(dataSourceId);
                table.setSchema(schema);
                tableList.add(table);
            }
            return tableList;
        } catch (SQLException e) {
            throw new MetaException("获取数据源连接失败：" + e);
        }
    }

    @Override
    public List<Column> getColumns(String dataSourceId, String catalog, String schema, String table) {
        DataSource dataSource = DataSourceHelper.getDataSource(dataSourceId);

        try (Connection connection = dataSource.getConnection()) {
            ResultSet resultSet = connection.getMetaData().getColumns(catalog, schema, table, null);

            List<Column> columnList = new ArrayList<>();
            while (resultSet.next()) {
                Column column = new Column();
                column.setDataSourceId(dataSourceId);
                column.setColumnName(resultSet.getString("COLUMN_NAME"));
                column.setRemarks(resultSet.getString("REMARKS"));
                column.setColumnType(resultSet.getString("TYPE_NAME"));
                column.setColumnProcessType(getTypeNameByColumn(column.getColumnType().toUpperCase()));
                column.setColumnSize(resultSet.getString("COLUMN_SIZE"));
                column.setDecimalDigits(resultSet.getString("DECIMAL_DIGITS"));
                column.setColumnDefault(resultSet.getString("COLUMN_DEF"));
                column.setCatalog(resultSet.getString("TABLE_CAT"));
                column.setSchema(schema);
                column.setTableName(table);
                columnList.add(column);
            }

            return columnList;
        } catch (SQLException e) {
            throw new MetaException("获取数据源连接失败：" + e);
        }
    }

    public String getTypeNameByColumn(String columnType) {
        List<String> integerType = Arrays.asList("INT", "INTEGER", "TINYINT", "SMALLINT", "MEDIUMINT", "BIGINT", "NUMBER");
        if (integerType.contains(columnType)) {
            return "Integer";
        } else if (columnType.contains("DOUBLE")) {
            return "Double";
        } else if (columnType.contains("FLOAT")) {
            return "Float";
        } else if (columnType.equals("DATE")) {
            return "Date";
        } else if (columnType.equals("DATETIME")) {
            return "DateTime";
        } else if (columnType.equals("TIMESTAMP")) {
            return "TimeStamp";
        } else if (columnType.contains("BLOB")) {
            return "Blob";
        } else if (columnType.contains("TEXT")) {
            return "Text";
        } else {
            return "String";
        }
    }

}
