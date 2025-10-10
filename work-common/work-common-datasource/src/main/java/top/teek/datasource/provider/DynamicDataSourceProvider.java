package top.teek.datasource.provider;

import top.teek.datasource.constant.DataSourceConstant;
import top.teek.datasource.enums.DataSourceType;
import top.teek.datasource.properties.DataSourceProperties;
import top.teek.utils.StringUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.provider.AbstractJdbcDataSourceProvider;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Teeker
 * @date 2023/12/26 21:01
 * @note 项目初始化时，从数据库中获取数据源信息进行加载
 */
@Slf4j
public class DynamicDataSourceProvider extends AbstractJdbcDataSourceProvider {
    private final DataSourceProperties dataSourceProperties;
    private final boolean loadDefaultDataSource;

    public DynamicDataSourceProvider(DefaultDataSourceCreator defaultDataSourceCreator, DataSourceProperties dataSourceProperties, boolean loadDefaultDataSource) {
        super(defaultDataSourceCreator, dataSourceProperties.getDriverClassName(), dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword());
        this.dataSourceProperties = dataSourceProperties;
        this.loadDefaultDataSource = loadDefaultDataSource;
    }

    @Override
    protected Map<String, DataSourceProperty> executeStmt(Statement statement) throws SQLException {
        Map<String, DataSourceProperty> map = new HashMap<>(8);

        // 添加默认主数据源
        if (loadDefaultDataSource) {
            map.put(DataSourceConstant.DS_MASTER, dataSourceProperties);
        }

        String dsName = dataSourceProperties.getDsName();
        String queryDsSql = dataSourceProperties.getSelectSql();
        if (StringUtil.hasEmpty(dsName, queryDsSql)) {
            return loadDefaultDataSource ? map : Collections.emptyMap();
        }

        // queryDsSql 如果不存在，则判断 dsName 查询数据源（queryDsSql 优先级高）
        if (StringUtil.hasText(dsName) && StringUtil.hasEmpty(queryDsSql)) {
            queryDsSql = "select * from " + dsName;
        }

        ResultSet rs = statement.executeQuery(queryDsSql);
        while (rs.next()) {
            String name = rs.getString(DataSourceConstant.DS_ID);
            // 不加载 master 数据源
            if (!name.equals(DataSourceConstant.DS_MASTER)) {
                String username = rs.getString(DataSourceConstant.DS_USER_NAME);
                String password = rs.getString(DataSourceConstant.DS_USER_PWD);
                String url = rs.getString(DataSourceConstant.DS_JDBC_URL);
                String driverClassName = rs.getString(DataSourceConstant.DS_DRIVER_CLASS_NAME);
                DataSourceProperty property = new DataSourceProperty();
                property.setUsername(username);
                property.setLazy(false);
                property.setPassword(password);
                property.setUrl(url);
                if (StringUtil.hasEmpty(driverClassName)) {
                    driverClassName = DataSourceType.getDriverClass(rs.getString(DataSourceConstant.DS_TYPE).toLowerCase());
                }
                property.setDriverClassName(driverClassName);

                // 默认使用 Druid 连接池
                property.setType(DruidDataSource.class);
                map.put(name, property);
            }
        }
        return map;
    }
}