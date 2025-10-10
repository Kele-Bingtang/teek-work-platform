package top.teek.ag.system.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import top.teek.ag.core.helper.AgHelper;
import top.teek.ag.core.helper.SqlHelper;
import top.teek.ag.system.mapper.ServiceColMapper;
import top.teek.ag.system.model.dto.ServiceColBatchUpdateDTO;
import top.teek.ag.system.model.dto.ServiceColDTO;
import top.teek.ag.system.model.dto.ServiceInfoDTO;
import top.teek.ag.system.model.po.DataSource;
import top.teek.ag.system.model.po.ServiceCol;
import top.teek.ag.system.model.po.ServiceInfo;
import top.teek.ag.system.model.vo.ServiceColVO;
import top.teek.ag.system.permission.PermissionHelper;
import top.teek.ag.system.service.DataSourceService;
import top.teek.ag.system.service.ServiceColService;
import top.teek.core.exception.ServiceException;
import top.teek.core.validate.validator.ValidList;
import top.teek.datasource.helper.DataSourceHelper;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;
import java.util.function.Function;

/**
 * @author Teeker
 * @date 2024-06-22 00:15:48
 * @note 针对表「t_service_col（服务列配置项表）」的数据库操作 Service 实现
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceColServiceImpl extends ServiceImpl<ServiceColMapper, ServiceCol> implements ServiceColService {

    private final DataSourceService dataSourceService;
    private final SqlSessionFactory sqlSessionFactory;

    @Override
    public List<ServiceColVO> listByServiceId(String serviceId) {
        List<ServiceCol> serviceColList = baseMapper.selectList(Wrappers.<ServiceCol>lambdaQuery()
                .eq(ServiceCol::getServiceId, serviceId)
                .orderByAsc(ServiceCol::getDisplaySeq));
        return MapstructUtil.convert(serviceColList, ServiceColVO.class);
    }

    @Override
    public TablePage<ServiceColVO> listPage(ServiceColDTO serviceColDTO, PageQuery pageQuery) {
        LambdaQueryWrapper<ServiceCol> wrapper = buildQueryWrapper(serviceColDTO);

        Page<ServiceCol> serviceColPage = baseMapper.selectPage(pageQuery.buildPage(), wrapper);
        return TablePage.build(serviceColPage, ServiceColVO.class);
    }

    private LambdaQueryWrapper<ServiceCol> buildQueryWrapper(ServiceColDTO serviceColDTO) {
        return Wrappers.<ServiceCol>lambdaQuery()
                .eq(StringUtil.hasText(serviceColDTO.getJsonCol()), ServiceCol::getJsonCol, serviceColDTO.getJsonCol())
                .eq(StringUtil.hasText(serviceColDTO.getServiceId()), ServiceCol::getServiceId, serviceColDTO.getServiceId())
                .eq(Objects.nonNull(serviceColDTO.getIsWhereKey()), ServiceCol::getIsWhereKey, serviceColDTO.getIsWhereKey())
                .orderByAsc(ServiceCol::getDisplaySeq);
    }

    @Override
    public boolean addServiceCol(ServiceColDTO serviceColDTO) {
        ServiceCol serviceCol = MapstructUtil.convert(serviceColDTO, ServiceCol.class);
        return baseMapper.insert(serviceCol) > 0;
    }

    @Override
    public boolean editServiceCol(ServiceColDTO serviceColDTO) {
        ServiceCol serviceCol = MapstructUtil.convert(serviceColDTO, ServiceCol.class);
        return baseMapper.updateById(serviceCol) > 0;
    }

    @Override
    public boolean removeServiceColById(String id) {
        ServiceCol serviceCol = baseMapper.selectById(id);
        PermissionHelper.checkProjectOperator(AgHelper.getUserId(), serviceCol.getProjectId(), "1h");

        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public boolean removeAllServiceColByServiceId(String serviceId) {
        return baseMapper.delete(Wrappers.<ServiceCol>lambdaQuery()
                .eq(ServiceCol::getServiceId, serviceId)) > 0;
    }

    @Override
    public boolean removeAllServiceColByServiceIdByCategoryId(String categoryId) {
        return baseMapper.delete(Wrappers.<ServiceCol>lambdaQuery()
                .eq(ServiceCol::getCategoryId, categoryId)) > 0;
    }

    @Override
    public boolean removeAllServiceColByServiceIdByProjectIdId(String projectId) {
        return baseMapper.delete(Wrappers.<ServiceCol>lambdaQuery()
                .eq(ServiceCol::getProjectId, projectId)) > 0;
    }

    @Override
    public boolean removeAllServiceColByServiceIdByTeamId(String teamId) {
        return baseMapper.delete(Wrappers.<ServiceCol>lambdaQuery()
                .eq(ServiceCol::getTeamId, teamId)) > 0;
    }

    @Override
    public boolean checkServiceColUnique(ServiceColDTO serviceColDTO) {
        return baseMapper.exists(Wrappers.<ServiceCol>lambdaQuery()
                .eq(ServiceCol::getServiceId, serviceColDTO.getServiceId())
                .and(e -> e.eq(ServiceCol::getTableCol, serviceColDTO.getTableCol())
                        .or()
                        .eq(ServiceCol::getJsonCol, serviceColDTO.getJsonCol())
                )
                .ne(Objects.nonNull(serviceColDTO.getId()), ServiceCol::getId, serviceColDTO.getId()));
    }

    /**
     * 构建列配置项信息
     *
     * @param resultSetMetaData SQL 返回的 ResultSetMetaData
     * @param serviceInfo       服务信息
     * @return 列配置项信息
     */
    private List<ServiceCol> buildServiceCol(ResultSetMetaData resultSetMetaData, ServiceInfo serviceInfo) {
        try {
            List<ServiceCol> serviceColList = new ArrayList<>();

            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                ServiceCol serviceCol = new ServiceCol();
                serviceCol.setTableCol(resultSetMetaData.getColumnName(i));
                String columnType = DataSourceHelper.jdbcMetaData.getTypeNameByColumn(resultSetMetaData.getColumnTypeName(i).toUpperCase());
                serviceCol.setColType(columnType);
                serviceCol.setColLength(resultSetMetaData.getPrecision(i));
                // 转为小驼峰
                String lowerCamelCase = CharSequenceUtil.toCamelCase(resultSetMetaData.getColumnName(i));
                serviceCol.setJsonCol(lowerCamelCase);
                serviceCol.setReportCol(lowerCamelCase);
                serviceCol.setTeamId(serviceInfo.getTeamId());
                serviceCol.setProjectId(serviceInfo.getProjectId());
                serviceCol.setCategoryId(serviceInfo.getCategoryId());
                serviceCol.setServiceId(serviceInfo.getServiceId());
                serviceColList.add(serviceCol);
            }
            return serviceColList;
        } catch (SQLException e) {
            throw new ServiceException("解析字段失败");
        }
    }

    @Override
    public Integer listColumnThenInsert(ServiceInfoDTO serviceInfoDTO) {
        // 切换数据源
        DataSourceHelper.use(serviceInfoDTO.getDataSourceId());
        ServiceInfo serviceInfo = MapstructUtil.convert(serviceInfoDTO, ServiceInfo.class);

        // 获取 SQL 返回的 ResultSetMetaData，目的是获取每个字段的信息
        ResultSetMetaData resultSetMetaData = getResultSetMetaData(serviceInfo);
        // 构建列配置项信息
        List<ServiceCol> serviceColList = buildServiceCol(resultSetMetaData, serviceInfo);

        Db.saveBatch(serviceColList);

        DataSourceHelper.close();
        return serviceColList.size();
    }

    @Override
    public Integer reGenCol(ServiceInfo serviceInfo) {
        if (StringUtil.hasEmpty(serviceInfo.getSelectSql())) {
            return -1;
        }
        DataSourceHelper.use(serviceInfo.getDataSourceId());
        ResultSetMetaData resultSetMetaData = getResultSetMetaData(serviceInfo);
        List<ServiceCol> newServiceColList = buildServiceCol(resultSetMetaData, serviceInfo);
        // 查询目前的列配置信息
        List<ServiceCol> oldServiceColList = baseMapper.selectList(Wrappers.<ServiceCol>lambdaQuery()
                .eq(ServiceCol::getServiceId, serviceInfo.getServiceId()));
        // 获取新增的列
        List<ServiceCol> difference = newServiceColList.stream().filter(s -> !oldServiceColList.contains(s)).toList();
        if (difference.isEmpty()) {
            return 0;
        }
        Db.saveBatch(difference);

        DataSourceHelper.close();
        return difference.size();
    }

    @Override
    public Integer removeInvalidCol(ServiceInfo serviceInfo) {
        if (StringUtil.hasEmpty(serviceInfo.getSelectSql())) {
            return -1;
        }
        DataSourceHelper.use(serviceInfo.getDataSourceId());
        ResultSetMetaData resultSetMetaData = getResultSetMetaData(serviceInfo);
        List<ServiceCol> newServiceColList = buildServiceCol(resultSetMetaData, serviceInfo);
        // 查询目前的列配置信息
        List<ServiceCol> oldServiceColList = baseMapper.selectList(Wrappers.<ServiceCol>lambdaQuery()
                .eq(ServiceCol::getServiceId, serviceInfo.getServiceId()));
        // 获取失效列配置项的 ID
        List<Long> ids = oldServiceColList.stream().filter(s -> !newServiceColList.contains(s)).map(ServiceCol::getId).toList();
        if (ids.isEmpty()) {
            return 0;
        }
        // 删除不存在的 ID
        Db.removeByIds(ids, ServiceCol.class);

        DataSourceHelper.close();
        return ids.size();
    }

    @Override
    public ResultSetMetaData getResultSetMetaData(ServiceInfo serviceInfo) {
        // 查询数据源信息
        DataSource dataSource = dataSourceService.getOne(Wrappers.<DataSource>lambdaQuery()
                .eq(DataSource::getDataSourceId, serviceInfo.getDataSourceId()));
        // SQL 添加 LIMIT 1，防止数据量过多导致查询效率慢
        String limitSql = SqlHelper.addLimitToSql(serviceInfo.getSelectSql(), 1, dataSource.getDataSourceType());
        return getResultSetMetaData(limitSql);
    }

    /**
     * 获取 ResultSetMetaData
     *
     * @param selectSql 查询 SQL
     * @return ResultSetMetaData
     */
    private ResultSetMetaData getResultSetMetaData(String selectSql) {
        ResultSetMetaData metaData;
        try (SqlSession sqlSession = sqlSessionFactory.openSession(); Connection connection = sqlSession.getConnection();
             Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(selectSql)) {
            metaData = resultSet.getMetaData();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException("SQL 无法执行，请检查 SQL 语法、表是否存在、表名前是否加上数据库名");
        }
        return metaData;
    }


    @Override
    public boolean checkExitCol(String serviceId) {
        return baseMapper.exists(Wrappers.<ServiceCol>lambdaQuery()
                .eq(ServiceCol::getServiceId, serviceId));
    }


    @Override
    public boolean editBatchServiceCol(ServiceColBatchUpdateDTO batchUpdateDTO) {
        ValidList<String> jsonColList = batchUpdateDTO.getJsonColList();
        // 转换成 MP 需要的批量更新的格式
        List<ServiceCol> serviceColList = new ArrayList<>();
        jsonColList.forEach(jsonCol -> {
            ServiceCol serviceCol = new ServiceCol();
            serviceCol.setJsonCol(jsonCol);
            serviceCol.setAllowInsert(batchUpdateDTO.getAllowInsert());
            serviceCol.setAllowUpdate(batchUpdateDTO.getAllowUpdate());
            serviceCol.setAllowRequest(batchUpdateDTO.getAllowRequest());
            serviceColList.add(serviceCol);
        });
        return updateBatchByColumn(serviceColList, serviceCol -> {
            LambdaUpdateWrapper<ServiceCol> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.eq(ServiceCol::getJsonCol, serviceCol.getJsonCol());
            return updateWrapper;
        });
    }

    private boolean updateBatchByColumn(Collection<ServiceCol> entityList, Function<ServiceCol, LambdaUpdateWrapper<ServiceCol>> queryWrapperFunction) {
        String sqlStatement = getSqlStatement(SqlMethod.UPDATE);
        return executeBatch(entityList, (sqlSession, entity) -> {
            Map<String, Object> param = CollectionUtils.newHashMapWithExpectedSize(8);
            param.put(Constants.ENTITY, entity);
            param.put(Constants.WRAPPER, queryWrapperFunction.apply(entity));
            sqlSession.update(sqlStatement, param);
        });
    }
}




