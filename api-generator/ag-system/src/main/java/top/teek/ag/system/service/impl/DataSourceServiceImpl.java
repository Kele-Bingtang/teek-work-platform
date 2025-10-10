package top.teek.ag.system.service.impl;

import top.teek.ag.core.enums.SqlGeneratorTemplate;
import top.teek.ag.core.helper.AgHelper;
import top.teek.ag.system.mapper.DataSourceMapper;
import top.teek.ag.system.mapper.base.SQLExecuteMapper;
import top.teek.ag.system.model.dto.DataSourceDTO;
import top.teek.ag.system.model.dto.SqlDTO;
import top.teek.ag.system.model.po.DataSource;
import top.teek.ag.system.model.vo.DataSourceTable;
import top.teek.ag.system.model.vo.DataSourceVO;
import top.teek.ag.system.permission.PermissionHelper;
import top.teek.ag.system.service.DataSourceService;
import top.teek.core.exception.ServerException;
import top.teek.core.exception.ServiceException;
import top.teek.datasource.helper.DataSourceHelper;
import top.teek.datasource.meta.Column;
import top.teek.datasource.meta.Schema;
import top.teek.datasource.meta.Table;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Teeker
 * @date 2024-06-30 21:00:59
 * @note 针对表「t_data_source（数据源配置表）」的数据库操作 Service 实现
 */
@Service
@RequiredArgsConstructor
public class DataSourceServiceImpl extends ServiceImpl<DataSourceMapper, DataSource> implements DataSourceService {

    private final SQLExecuteMapper sqlExecuteMapper;

    @Override
    public TablePage<DataSourceVO> listPage(DataSourceDTO dataSourceDTO, PageQuery pageQuery) {
        LambdaQueryWrapper<DataSource> dataSourceLambdaQueryWrapper = buildQueryWrapper(dataSourceDTO);
        Page<DataSource> dataSourcePage = baseMapper.selectPage(pageQuery.buildPage(), dataSourceLambdaQueryWrapper);
        TablePage<DataSourceVO> dataSourceVOTablePage = TablePage.build(dataSourcePage, DataSourceVO.class);
        dataSourceVOTablePage.getList().forEach(dataSource -> {
            dataSource.setDataSourceTypeDriveClass(Arrays.asList(dataSource.getDataSourceType(), dataSource.getDriverClassName()));
        });
        return dataSourceVOTablePage;
    }

    @Override
    public List<DataSourceVO> listSelect(String teamId) {
        List<DataSource> dataSourceList = baseMapper.selectList(Wrappers.<DataSource>lambdaQuery()
                .select(DataSource::getDataSourceId, DataSource::getDataSourceName)
                .eq(DataSource::getTeamId, teamId));

        return MapstructUtil.convert(dataSourceList, DataSourceVO.class);
    }

    @Override
    public List<DataSourceVO> listByProjectId(String projectId) {
        List<DataSource> dataSourceList = baseMapper.listByProjectId(projectId);
        return MapstructUtil.convert(dataSourceList, DataSourceVO.class);
    }

    private LambdaQueryWrapper<DataSource> buildQueryWrapper(DataSourceDTO dataSourceDTO) {
        return Wrappers.<DataSource>lambdaQuery()
                .eq(StringUtil.hasText(dataSourceDTO.getTeamId()), DataSource::getTeamId, dataSourceDTO.getTeamId())
                .eq(StringUtil.hasText(dataSourceDTO.getDataSourceName()), DataSource::getDataSourceName, dataSourceDTO.getDataSourceName())
                .eq(StringUtil.hasText(dataSourceDTO.getDataSourceType()), DataSource::getDataSourceType, dataSourceDTO.getDataSourceType())
                .eq(Objects.nonNull(dataSourceDTO.getStatus()), DataSource::getStatus, dataSourceDTO.getStatus())
                .orderByDesc(DataSource::getCreateTime);
    }

    @Override
    public Boolean addDataSource(DataSourceDTO dataSourceDTO) {
        DataSource dateSource = MapstructUtil.convert(dataSourceDTO, DataSource.class);
        if (Objects.isNull(dateSource.getDataSourceType())) {
            try {
                URI uri = new URI(dateSource.getJdbcUrl().replace("jdbc:", ""));
                dateSource.setDataSourceType(uri.getScheme());
            } catch (URISyntaxException e) {
                throw new ServerException("JDBC URL 格式不正确");
            }
        }
        String driverClass = DataSourceHelper.getDriverClass(dataSourceDTO.getDataSourceType());
        if (StringUtil.hasEmpty(dataSourceDTO.getDriverClassName())) {
            dateSource.setDriverClassName(driverClass);
        }

        return baseMapper.insert(dateSource) > 0;
    }

    @Override
    public Boolean editDataSource(DataSourceDTO dataSourceDTO) {
        DataSource dateSource = MapstructUtil.convert(dataSourceDTO, DataSource.class);
        if (Objects.isNull(dateSource.getDataSourceType())) {
            try {
                URI uri = new URI(dateSource.getJdbcUrl().replace("jdbc:", ""));
                dateSource.setDataSourceType(uri.getScheme());
            } catch (URISyntaxException e) {
                throw new ServerException("JDBC URL 格式不正确");
            }
        }
        String driverClass = DataSourceHelper.getDriverClass(dataSourceDTO.getDataSourceType());
        if (StringUtil.hasEmpty(dataSourceDTO.getDriverClassName())) {
            dateSource.setDriverClassName(driverClass);
        }

        return baseMapper.updateById(dateSource) > 0;
    }

    @Override
    public Boolean removeDataSource(String dataSourceId) {
        DataSource dataSource = baseMapper.selectOne(Wrappers.<DataSource>lambdaQuery()
                .eq(DataSource::getDataSourceId, dataSourceId));

        PermissionHelper.checkTeamOwnerAndAdmin(AgHelper.getUserId(), dataSource.getTeamId(), "1h");
        return baseMapper.delete(Wrappers.<DataSource>lambdaQuery()
                .eq(DataSource::getDataSourceId, dataSourceId)) > 0;
    }

    @Override
    public boolean checkDataSourceNameUnique(DataSourceDTO dataSourceDTO) {
        return baseMapper.exists(Wrappers.<DataSource>lambdaQuery()
                .eq(DataSource::getDataSourceName, dataSourceDTO.getDataSourceName())
                .ne(Objects.nonNull(dataSourceDTO.getId()), DataSource::getId, dataSourceDTO.getId()));
    }

    @Override
    public boolean testConnect(DataSourceDTO dataSourceDTO) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername(dataSourceDTO.getUsername());
        hikariDataSource.setPassword(dataSourceDTO.getPassword());
        hikariDataSource.setJdbcUrl(dataSourceDTO.getJdbcUrl());
        if (StringUtil.hasEmpty(dataSourceDTO.getDriverClassName())) {
            hikariDataSource.setDriverClassName(DataSourceHelper.getDriverClass(dataSourceDTO.getDataSourceType()));
        }
        return DataSourceHelper.connect(hikariDataSource);
    }

    @Override
    public List<String> listSchemaByDataSource(String dataSourceId) {
        List<Schema> catalogs = DataSourceHelper.getCatalogs(dataSourceId);
        return catalogs.stream().map(Schema::getSchema).toList();
    }

    @Override
    public List<DataSourceTable> listTableBySchema(String dataSourceId, String schema) {
        List<Table> tableList = DataSourceHelper.getTables(dataSourceId, schema, null);
        Map<String, List<String>> tableMap = tableList.stream()
                .collect(Collectors.groupingBy(Table::getTableType, Collectors.mapping(Table::getTableName, Collectors.toList())));

        List<DataSourceTable> dataSourceTableList = new ArrayList<>();
        tableMap.forEach((key, value) -> {
            // 不添加存储过程
            if (!"PROCEDURE".equals(key)) {
                DataSourceTable dataSourceTable = new DataSourceTable();
                dataSourceTable.setTableType(key);
                dataSourceTable.setTableNameList(value);
                dataSourceTableList.add(dataSourceTable);
            }
        });
        return dataSourceTableList;
    }

    @Override
    public List<String> listColumnBySchema(String dataSourceId, String schema, String table) {
        List<Column> columnList = DataSourceHelper.getColumns(dataSourceId, schema, schema, table);
        return columnList.stream().map(Column::getColumnName).toList();
    }

    @Override
    public List<LinkedHashMap<String, Object>> executeSelect(SqlDTO sqlDTO) {
        // 切换数据源
        DataSourceHelper.use(sqlDTO.getDataSourceId());
        return sqlExecuteMapper.executeSelectList(sqlDTO.getSql(), null);
    }

    @Override
    public String generateTemple(String dataSourceId, String schema, String tableName, String type) {
        if (SqlGeneratorTemplate.SELECT.name().equalsIgnoreCase(type)) {
            List<String> columnNameList = listColumnBySchema(dataSourceId, schema, tableName);
            String columns = String.join(", ", columnNameList);
            return "SELECT " + columns + "\t\nFROM " + schema + "." + tableName;
        }
        throw new ServiceException("暂不支持" + type + "类型");
    }
}


