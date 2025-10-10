package top.teek.datasource.helper;

import top.teek.datasource.enums.DataSourceType;
import top.teek.datasource.meta.Column;
import top.teek.datasource.meta.JdbcMetaData;
import top.teek.datasource.meta.Schema;
import top.teek.datasource.meta.Table;
import top.teek.datasource.properties.DataSourceProperties;
import top.teek.helper.SpringHelper;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * @author Teeker
 * @date 2023/12/26 21:03
 * @note 数据源工具类
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataSourceHelper {
    private static final DataSource dataSource = SpringHelper.getBean(DataSource.class);
    private static final DefaultDataSourceCreator defaultDataSourceCreator = SpringHelper.getBean(DefaultDataSourceCreator.class);
    public static final JdbcMetaData jdbcMetaData = new JdbcMetaData();

    /**
     * 获取指定的数据源信息
     *
     * @param dataSourceId 数据源名字
     */
    public static DataSource getDataSource(String dataSourceId) {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        return ds.getDataSource(dataSourceId);
    }

    /**
     * 获取所有数据源信息
     */
    public static Map<String, DataSource> getDataSource() {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        return ds.getDataSources();
    }

    /**
     * 添加一个数据源
     */
    public static void addDataSource(DataSourceProperties dataSourceProperties) {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        DataSource newDataSource = defaultDataSourceCreator.createDataSource(dataSourceProperties);
        ds.addDataSource(dataSourceProperties.getPoolName(), newDataSource);
    }

    /**
     * 删除一个数据源
     */
    public static void removeDataSource(String dataSourceId) {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        ds.removeDataSource(dataSourceId);
    }

    /**
     * 设置当前线程数据源，与数据库交互前可以切换数据源
     *
     * @param dataSourceId 数据源名字
     */
    public static void use(String dataSourceId) {
        DynamicDataSourceContextHolder.push(dataSourceId);
    }

    /**
     * 关闭当前线程数据源
     */
    public static void close() {
        DynamicDataSourceContextHolder.clear();
    }

    /**
     * 测试连接
     *
     * @param dataSourceProperty 数据源属性
     * @return 连接是否成功
     */
    public static boolean connect(DataSourceProperty dataSourceProperty) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername(dataSourceProperty.getUsername());
        druidDataSource.setPassword(dataSourceProperty.getPassword());
        druidDataSource.setUrl(dataSourceProperty.getUrl());
        druidDataSource.setDriverClassName(dataSourceProperty.getDriverClassName());
        return connect(druidDataSource);
    }

    /**
     * 测试连接
     *
     * @param dataSource 数据源
     * @return 连接是否成功
     */
    public static boolean connect(DataSource dataSource) {
        try (Connection ignored = dataSource.getConnection()) {
            log.info("连接成功");
        } catch (Exception e) {
            log.info("连接失败：{}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 获取数据库驱动类路径
     *
     * @param datasourceType 数据源类型
     * @return 数据库驱动类路径
     */
    public static String getDriverClass(String datasourceType) {
        return DataSourceType.getDriverClass(datasourceType.toLowerCase());
    }

    /**
     * 获取数据源 Catalog
     *
     * @param dataSourceId 数据源 ID
     * @return 数据源 Catalog
     */
    public static List<Schema> getCatalogs(String dataSourceId) {
        return jdbcMetaData.getCatalogs(dataSourceId);
    }

    /**
     * 获取数据源 Schema
     *
     * @param dataSourceId 数据源 ID
     * @param catalog        Catalog
     * @return 数据源 Schema
     */
    public static List<Schema> getSchemas(String dataSourceId, String catalog) {
        return jdbcMetaData.getSchemas(dataSourceId, catalog);
    }

    /**
     * 获取数据表列表
     *
     * @param dataSourceId 数据源 ID
     * @param catalog        Catalog
     * @param schema         模式
     * @return 数据表列表
     */
    public static List<Table> getTables(String dataSourceId, String catalog, String schema) {
        return jdbcMetaData.getTables(dataSourceId, catalog, schema);
    }

    /**
     * 获取数据表字段列表
     *
     * @param dataSourceId 数据源 ID
     * @param catalog        Catalog
     * @param schema         模式
     * @param table          表名
     * @return 数据表字段列表
     */
    public static List<Column> getColumns(String dataSourceId, String catalog, String schema, String table) {
        return jdbcMetaData.getColumns(dataSourceId, catalog, schema, table);
    }
}
