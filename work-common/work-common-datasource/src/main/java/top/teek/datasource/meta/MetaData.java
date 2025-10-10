package top.teek.datasource.meta;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/6/30 17:49:27
 * @note
 */
public interface MetaData {

    /**
     * 获取数据源 Catalog
     *
     * @param dataSourceId 数据源 ID
     * @return 数据源 Catalog
     */
    List<Schema> getCatalogs(String dataSourceId);

    /**
     * 获取数据源 Schema
     *
     * @param dataSourceId 数据源 ID
     * @param catalog        Catalog
     * @return 数据源 Schema
     */
    List<Schema> getSchemas(String dataSourceId, String catalog);

    /**
     * 获取数据表列表
     *
     * @param dataSourceId 数据源 ID
     * @param catalog        Catalog
     * @param schema         模式
     * @return 数据表列表
     */
    List<Table> getTables(String dataSourceId, String catalog, String schema);

    /**
     * 获取数据表字段列表
     *
     * @param dataSourceId 数据源 ID
     * @param catalog        Catalog
     * @param schema         模式
     * @param table          表名
     * @return 数据表字段列表
     */
    List<Column> getColumns(String dataSourceId, String catalog, String schema, String table);


    /**
     * 根据数据库列类型获取自定义列类型名称
     *
     * @param columnType 列类型
     * @return 列类型名称
     */
    String getTypeNameByColumn(String columnType);

}
