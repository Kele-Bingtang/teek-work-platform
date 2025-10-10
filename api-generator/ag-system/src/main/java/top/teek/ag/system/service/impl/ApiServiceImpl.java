package top.teek.ag.system.service.impl;

import top.teek.ag.core.enums.QueryFilterType;
import top.teek.ag.core.helper.SqlHelper;
import top.teek.ag.system.mapper.DataSourceMapper;
import top.teek.ag.system.mapper.ProjectMapper;
import top.teek.ag.system.mapper.ServiceColMapper;
import top.teek.ag.system.mapper.ServiceInfoMapper;
import top.teek.ag.system.mapper.base.SQLExecuteMapper;
import top.teek.ag.system.model.bo.BatchOperateBO;
import top.teek.ag.system.model.bo.ColumnBO;
import top.teek.ag.system.model.bo.WhereBO;
import top.teek.ag.system.model.po.DataSource;
import top.teek.ag.system.model.po.Project;
import top.teek.ag.system.model.po.ServiceCol;
import top.teek.ag.system.model.po.ServiceInfo;
import top.teek.ag.system.service.ApiService;
import top.teek.core.http.Assert;
import top.teek.core.exception.ServerException;
import top.teek.core.exception.ServiceException;
import top.teek.datasource.helper.DataSourceHelper;
import top.teek.excel.helper.ExcelHelper;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.utils.JacksonUtil;
import top.teek.utils.ListUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Teeker
 * @date 2024/7/7 17:25:49
 * @note
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ApiServiceImpl implements ApiService {
    private final DataSourceMapper dataSourceMapper;
    private final ProjectMapper projectMapper;
    private final ServiceColMapper serviceColMapper;
    private final ServiceInfoMapper serviceInfoMapper;
    private final SQLExecuteMapper sqlExecuteMapper;

    private final static String RESPONSE_DATA_ROOT = "_root";

    @Override
    public LinkedHashMap<String, Object> getOne(String apiUri, String secretKey, Map<String, Object> requestParamsMap) {
        List<LinkedHashMap<String, Object>> linkedHashMapList = list(apiUri, secretKey, requestParamsMap, 1);
        return linkedHashMapList.get(0);
    }

    @Override
    public List<LinkedHashMap<String, Object>> list(String apiUri, String secretKey, Map<String, Object> requestParamsMap, Integer limit) {
        // 验证项目，获取服务信息
        ServiceInfo serviceInfo = checkThenGetService(apiUri, secretKey);
        DataSourceHelper.use(serviceInfo.getDataSourceId());

        // 获取服务列配置项信息
        List<ServiceCol> serviceColList = serviceColMapper.selectList(Wrappers.<ServiceCol>lambdaQuery()
                .eq(ServiceCol::getServiceId, serviceInfo.getServiceId()));
        Assert.isTrue(!ListUtil.isEmpty(serviceColList), "不存在列配置项");

        String selectSql = serviceInfo.getSelectSql();
        // 降级处理
        if (StringUtil.hasEmpty(selectSql)) {
            DataSourceHelper.close();
            return getBreakingRespond(serviceInfo.getBreakingRespond());
        }

        String fullSelectSql = getFullSelectSql(selectSql, requestParamsMap, serviceColList);

        if (Objects.nonNull(limit)) {
            // 查询数据源信息
            DataSource dataSource = dataSourceMapper.selectOne(Wrappers.<DataSource>lambdaQuery()
                    .eq(DataSource::getDataSourceId, serviceInfo.getDataSourceId()));
            fullSelectSql = SqlHelper.addLimitToSql(fullSelectSql, limit, dataSource.getDataSourceType());
        }

        List<LinkedHashMap<String, Object>> linkedHashMap;
        try {
            // fullSelectSql = SqlHelper.processMybatisParams(fullSelectSql);
            linkedHashMap = sqlExecuteMapper.executeSelectList(fullSelectSql, requestParamsMap);
        } catch (Exception e) {
            log.info("SQL 执行失败：", e);
            DataSourceHelper.close();
            // SQL 执行失败，则执行降级处理
            return getBreakingRespond(serviceInfo.getBreakingRespond());
        }
        DataSourceHelper.close();

        if (ListUtil.isEmpty(linkedHashMap)) {
            return new ArrayList<>();
        }

        List<LinkedHashMap<String, Object>> dataList = processMapping(linkedHashMap, serviceColList);

        String responseTemplate = serviceInfo.getResponseTemplate();
        if (StringUtil.hasText(responseTemplate) && JacksonUtil.canSerialize(responseTemplate)) {
            Map<String, Object> jsonTemplate = JacksonUtil.toJson(responseTemplate, Map.class);
            if (Objects.nonNull(jsonTemplate.get(RESPONSE_DATA_ROOT)) && jsonTemplate.size() == 1) {
                return dataList;
            }
            return transformData(dataList, jsonTemplate);
        }
        return dataList;
    }

    @Override
    public TablePage<LinkedHashMap<String, Object>> page(String apiUri, String secretKey, Map<String, Object> requestParamsMap, PageQuery pageQuery) {
        // 验证项目，获取服务信息
        ServiceInfo serviceInfo = checkThenGetService(apiUri, secretKey);
        DataSourceHelper.use(serviceInfo.getDataSourceId());

        // 获取服务列配置项信息
        List<ServiceCol> serviceColList = serviceColMapper.selectList(Wrappers.<ServiceCol>lambdaQuery()
                .eq(ServiceCol::getServiceId, serviceInfo.getServiceId()));
        Assert.isTrue(!ListUtil.isEmpty(serviceColList), "不存在列配置项");

        String selectSql = serviceInfo.getSelectSql();
        // 降级处理
        if (Objects.isNull(selectSql)) {
            DataSourceHelper.close();
            return TablePage.build(getBreakingRespond(serviceInfo.getBreakingRespond()));
        }

        String fullSelectSql = getFullSelectSql(selectSql, requestParamsMap, serviceColList);

        Page<LinkedHashMap<String, Object>> linkedHashMapPage;
        try {
            linkedHashMapPage = sqlExecuteMapper.executeSelectPage(pageQuery.buildPage(), fullSelectSql, requestParamsMap);
        } catch (Exception e) {
            log.info("SQL 执行失败：", e);
            DataSourceHelper.close();
            // SQL 执行失败，则执行降级处理
            return TablePage.build(getBreakingRespond(serviceInfo.getBreakingRespond()));
        }

        List<LinkedHashMap<String, Object>> linkedHashMapList = linkedHashMapPage.getRecords();

        List<LinkedHashMap<String, Object>> processMappingList = processMapping(linkedHashMapList, serviceColList);
        DataSourceHelper.close();

        String responseTemplate = serviceInfo.getResponseTemplate();
        if (StringUtil.hasText(responseTemplate) && JacksonUtil.canSerialize(responseTemplate)) {
            Map<String, Object> jsonTemplate = JacksonUtil.toJson(responseTemplate, Map.class);
            if (Objects.nonNull(jsonTemplate.get(RESPONSE_DATA_ROOT)) && jsonTemplate.size() == 1) {
                linkedHashMapPage.setRecords(processMappingList);
                return TablePage.build(linkedHashMapPage);
            }
            linkedHashMapPage.setRecords(transformData(processMappingList, jsonTemplate));
            return TablePage.build(linkedHashMapPage);
        }

        return TablePage.build(linkedHashMapPage);
    }

    @Override
    public List<LinkedHashMap<String, Object>> listByServiceId(String serviceId, Map<String, Object> requestParamsMap) {
        ServiceInfo serviceInfo = serviceInfoMapper.selectOne(Wrappers.<ServiceInfo>lambdaQuery()
                .eq(ServiceInfo::getServiceId, serviceId));
        Project project = projectMapper.selectOne(Wrappers.<Project>lambdaQuery()
                .eq(Project::getProjectId, serviceInfo.getProjectId()));
        return list(serviceInfo.getFullUrl(), project.getSecretKey(), requestParamsMap, null);
    }

    @Override
    public TablePage<LinkedHashMap<String, Object>> pageByServiceId(String serviceId, PageQuery pageQuery, Map<String, Object> requestParamsMap) {
        ServiceInfo serviceInfo = serviceInfoMapper.selectOne(Wrappers.<ServiceInfo>lambdaQuery()
                .eq(ServiceInfo::getServiceId, serviceId));
        Project project = projectMapper.selectOne(Wrappers.<Project>lambdaQuery()
                .eq(Project::getProjectId, serviceInfo.getProjectId()));

        return page(serviceInfo.getFullUrl(), project.getSecretKey(), requestParamsMap, pageQuery);
    }

    @Override
    public void export(String serviceId, PageQuery pageQuery, Map<String, Object> requestParamsMap, HttpServletResponse response) {
        ServiceInfo serviceInfo = serviceInfoMapper.selectOne(Wrappers.<ServiceInfo>lambdaQuery()
                .eq(ServiceInfo::getServiceId, serviceId));

        // 验证项目，获取服务信息
        DataSourceHelper.use(serviceInfo.getDataSourceId());

        // 获取服务列配置项信息
        List<ServiceCol> serviceColList = serviceColMapper.selectList(Wrappers.<ServiceCol>lambdaQuery()
                .eq(ServiceCol::getServiceId, serviceInfo.getServiceId()));
        Assert.isTrue(!ListUtil.isEmpty(serviceColList), "不存在列配置项");

        String selectSql = serviceInfo.getSelectSql();
        if (Objects.isNull(selectSql)) {
            DataSourceHelper.close();
            throw new ServiceException("SQL 为空，无法导出");
        }

        String fullSelectSql = getFullSelectSql(selectSql, requestParamsMap, serviceColList);

        Page<LinkedHashMap<String, Object>> linkedHashMapPage;
        try {
            linkedHashMapPage = sqlExecuteMapper.executeSelectPage(pageQuery.buildPage(), fullSelectSql, requestParamsMap);
        } catch (Exception e) {
            log.info("SQL 执行失败：", e);
            DataSourceHelper.close();
            throw new ServiceException("SQL 为空，无法导出");
        }

        List<LinkedHashMap<String, Object>> linkedHashMapList = linkedHashMapPage.getRecords();

        List<LinkedHashMap<String, Object>> processMappingList = processMapping(linkedHashMapList, serviceColList);
        DataSourceHelper.close();

        // 构建导出的表头
        List<List<String>> headerListList = new ArrayList<>();
        serviceColList.forEach(serviceCol -> headerListList.add(ListUtil.newArrayList(serviceCol.getReportCol())));

        // 构建导出的列表
        List<List<Object>> dataList = processMappingList.stream().map(linkedHashMap -> linkedHashMap.values().stream().toList()).toList();

        ExcelHelper.exportExcel(dataList, serviceInfo.getServiceName(), headerListList, response);
    }

    /**
     * 验证项目，获取服务信息
     *
     * @param requestUri 请求地址
     * @param secretKey  密钥
     * @return 服务信息
     */
    private ServiceInfo checkThenGetService(String requestUri, String secretKey) {
        Project project = projectMapper.selectOne(Wrappers.<Project>lambdaQuery()
                .eq(Project::getSecretKey, secretKey));
        Assert.nonNull(project, "项目不存在");
        Assert.isTrue(Objects.nonNull(project) && Objects.equals(project.getStatus(), STATUS_NORMAL), "该项目已被禁用");

        String baseUrl = project.getBaseUrl();
        if (requestUri.length() < baseUrl.length()) {
            throw new ServerException("服务链接和项目密钥不匹配");
        }

        String projectUrl = requestUri.substring(0, baseUrl.length());
        String serviceUrl = requestUri.substring(baseUrl.length());

        Assert.isTrue(projectUrl.equals(baseUrl), "服务链接和项目密钥不匹配");

        ServiceInfo serviceInfo = serviceInfoMapper.selectOne(Wrappers.<ServiceInfo>lambdaQuery()
                .eq(ServiceInfo::getProjectId, project.getProjectId())
                .eq(ServiceInfo::getServiceUrl, serviceUrl)
        );

        Assert.nonNull(serviceInfo, "服务不存在");
        Assert.isTrue(Objects.equals(serviceInfo.getStatus(), STATUS_NORMAL), "该服务已被禁用");
        return serviceInfo;
    }

    /**
     * SQL 为空或者执行失败，则执行降级处理
     *
     * @param breakingRespond 降级处理内容
     * @return 降级处理内容
     */
    private List<LinkedHashMap<String, Object>> getBreakingRespond(String breakingRespond) {
        Assert.notBlank(breakingRespond, "SQL 为空，因此不存在数据返回");
        if (breakingRespond.startsWith("{")) {
            return Collections.singletonList(JacksonUtil.toJson(breakingRespond, LinkedHashMap.class));
        }
        if (breakingRespond.startsWith("[")) {
            return JacksonUtil.toListMap(breakingRespond);
        }

        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("response", breakingRespond);

        return List.of(linkedHashMap);
    }

    /**
     * 获取完整的 select sql
     *
     * @param selectSql        select sql
     * @param requestParamsMap 查询参数
     * @param serviceColList   列配置项
     * @return 完整的 select sql
     */
    private String getFullSelectSql(String selectSql, Map<String, Object> requestParamsMap, List<ServiceCol> serviceColList) {
        // 获取查询条件
        String querySql = getQuerySql(requestParamsMap, serviceColList);
        // 获取 orderBy 排序
        String orderBySql = getOrderBySql(serviceColList);

        // 查询和分页处理，直接将 selectSql 包起来进行查询和分页
        return "SELECT a.* FROM (" + selectSql + ") a" + querySql + orderBySql;
    }

    /**
     * 获取 where 后的查询 sql
     *
     * @param requestParamsMap 字段数据
     * @param serviceColList   字段集合
     * @return where 后的查询 sql
     */
    private String getQuerySql(Map<String, Object> requestParamsMap, List<ServiceCol> serviceColList) {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>(requestParamsMap);
        linkedHashMap.remove("pageNum");
        linkedHashMap.remove("pageSize");
        StringBuilder querySql = new StringBuilder();
        for (Map.Entry<String, Object> entry : linkedHashMap.entrySet()) {
            for (ServiceCol serviceCol : serviceColList) {
                if (!entry.getKey().contains(serviceCol.getJsonCol()) || (Objects.isNull(serviceCol.getQueryFilter()) || serviceCol.getQueryFilter().equals(QueryFilterType.NO_FILTER.getIndex()))) {
                    continue;
                }

                Object value = entry.getValue();
                String name = QueryFilterType.getName(serviceCol.getQueryFilter());
                Assert.notBlank(name, "查询条件暂不支持");

                querySql.append(serviceCol.getTableCol());
                if (value instanceof String val) {
                    querySql.append(name.replace("{val}", val));
                } else if (value instanceof String[] val) {
                    // BETWEEN 范围查询
                    if (serviceCol.getQueryFilter() == 10) {
                        querySql.append(name.replace("{val0}", val[0]).replace("{val1}", val[1]));
                    }
                    // IN 查询
                    if (serviceCol.getQueryFilter() == 11) {
                        String inCondition = String.join(",", val);
                        querySql.append(name.replace("{val}", inCondition));
                    }
                }
                querySql.append(AND);
                break;
            }
        }
        if (!querySql.isEmpty()) {
            // 删除尾部的 AND
            querySql.delete(querySql.length() - AND.length(), querySql.length());
            return WHERE + querySql;
        }

        return "";
    }

    /**
     * 获取 order by sql
     *
     * @param serviceColList 字段结婚
     * @return order by sql
     */
    private String getOrderBySql(List<ServiceCol> serviceColList) {
        StringBuilder orderBySql = new StringBuilder();
        Stream<ServiceCol> serviceColStream = serviceColList.stream().sorted(Comparator.comparing(x -> Math.abs(x.getOrderBy())));
        serviceColStream.forEach(serviceColVO -> {
            Integer orderBy = serviceColVO.getOrderBy();
            // Order by 顺序：0 - 99
            if (orderBy > -99 && orderBy < 0) {
                orderBySql.append("'").append(serviceColVO.getTableCol()).append("'").append(" desc").append(", ");
            } else if (orderBy > 0 && orderBy < 99) {
                orderBySql.append("'").append(serviceColVO.getTableCol()).append("'").append(" asc").append(", ");
            }
        });
        // 去除结尾的 , 
        if (!orderBySql.isEmpty()) {
            orderBySql.delete(orderBySql.length() - 2, orderBySql.length());
            return ORDER_BY + orderBySql;
        }
        return "";
    }

    private List<LinkedHashMap<String, Object>> processMapping(List<LinkedHashMap<String, Object>> linkedHashMapList, List<ServiceCol> serviceColList) {
        List<LinkedHashMap<String, Object>> newListMap = new ArrayList<>(linkedHashMapList.size());
        Map<String, String> cacheMapping = serviceColList.stream()
                .filter(serviceCol -> serviceCol.getAllowRequest() != 0)
                .collect(Collectors.toMap(ServiceCol::getTableCol, ServiceCol::getJsonCol, (k1, k2) -> k1));

        for (LinkedHashMap<String, Object> linkedHashMap : linkedHashMapList) {
            LinkedHashMap<String, Object> newMap = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry : linkedHashMap.entrySet()) {
                if (cacheMapping.containsKey(entry.getKey())) {
                    newMap.put(cacheMapping.get(entry.getKey()), entry.getValue());
                } else if (Objects.nonNull(cacheMapping.get(entry.getKey()))) {
                    // cacheMapping 存在的，数据里不存在的也要添加进去
                    newMap.put(cacheMapping.get(entry.getKey()), null);
                }
            }
            newListMap.add(newMap);
        }

        return newListMap;
    }

    private static List<LinkedHashMap<String, Object>> transformData(List<LinkedHashMap<String, Object>> originalData, Map<String, Object> template) {
        List<LinkedHashMap<String, Object>> result = new ArrayList<>();
        for (LinkedHashMap<String, Object> row : originalData) {
            LinkedHashMap<String, Object> newRow = new LinkedHashMap<>();
            processTemplate(template, row, newRow);
            result.add(newRow);
        }
        return result;
    }

    private static void processTemplate(Map<String, Object> template, Map<String, Object> row, Map<String, Object> newRow) {
        for (Map.Entry<String, Object> entry : template.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (RESPONSE_DATA_ROOT.equals(key)) {
                // 处理 _root，_root 表示不需要包起来
                List<String> fields = (List<String>) value;
                for (String field : fields) {
                    newRow.put(field, row.get(field));
                }
            } else if (value instanceof List) {
                List<String> fields = (List<String>) value;
                Map<String, Object> nestedObj = new LinkedHashMap<>();
                for (String field : fields) {
                    nestedObj.put(field, row.get(field));
                }
                newRow.put(key, nestedObj);
            } else if (value instanceof Map) {
                // 递归处理嵌套对象
                Map<String, Object> nestedObj = new LinkedHashMap<>();
                newRow.put(key, nestedObj);
                processTemplate((Map<String, Object>) value, row, nestedObj);
            }
        }
    }

    @Override
    public Integer operate(String operateType, String apiUri, String secretKey, Map<String, Object> dataMap, List<Map<String, Object>> jsonList) {
        // 验证项目，获取服务信息
        ServiceInfo serviceInfo = checkThenGetService(apiUri, secretKey);
        DataSourceHelper.use(serviceInfo.getDataSourceId());

        // 获取服务列配置项信息
        List<ServiceCol> serviceColList = serviceColMapper.selectList(Wrappers.<ServiceCol>lambdaQuery()
                .eq(ServiceCol::getServiceId, serviceInfo.getServiceId()));
        Assert.isTrue(!ListUtil.isEmpty(serviceColList), "不存在列配置项");

        String selectSql = serviceInfo.getSelectSql();
        Assert.notBlank(selectSql, "SQL 为空，因此不存在数据返回");

        if (dataMap.isEmpty() && ListUtil.isEmpty(jsonList)) {
            throw new ServerException("数据为空");
        }

        // 默认值数据
        Map<String, String> defaultValueMap = serviceColList.stream().filter(serviceCol -> serviceCol.getDefaultValue() != null)
                .collect(Collectors.toMap(ServiceCol::getJsonCol, ServiceCol::getDefaultValue, (k1, k2) -> k1));

        // 单个数据模式，如 x-www-form-urlencoded、application/json 对象
        if (ListUtil.isEmpty(jsonList) || jsonList.size() == 1) {
            return processOneData(operateType, serviceInfo, serviceColList, ListUtil.isEmpty(jsonList) ? dataMap : jsonList.get(0), defaultValueMap);
        }

        // 批量模式，仅限 application/json 数组
        return processMultipleData(operateType, serviceInfo, serviceColList, jsonList, defaultValueMap);
    }

    @Override
    public Integer operateByServiceId(String operateType, String serviceId, Map<String, Object> dataMap) {
        ServiceInfo serviceInfo = serviceInfoMapper.selectOne(Wrappers.<ServiceInfo>lambdaQuery()
                .eq(ServiceInfo::getServiceId, serviceId));
        Project project = projectMapper.selectOne(Wrappers.<Project>lambdaQuery()
                .eq(Project::getProjectId, serviceInfo.getProjectId()));

        return operate(operateType, serviceInfo.getFullUrl(), project.getSecretKey(), dataMap, null);
    }

    /**
     * 处理一笔数据的增删改
     *
     * @param operateType     操作类型
     * @param serviceInfo     服务信息
     * @param serviceColList  类配置信息
     * @param dataMap         数据
     * @param defaultValueMap 默认值
     * @return 影响行数
     */
    private Integer processOneData(String operateType, ServiceInfo serviceInfo, List<ServiceCol> serviceColList, Map<String, Object> dataMap, Map<String, String> defaultValueMap) {
        return switch (operateType) {
            case INSERT -> {
                // 过滤不需要操作的列配置项
                Map<String, ServiceCol> cacheNeedMapping = serviceColList.stream().filter(serviceCol -> (serviceCol.getAllowInsert() != 0))
                        .collect(Collectors.toMap(ServiceCol::getJsonCol, serviceCol -> serviceCol, (k1, k2) -> k1));
                BatchOperateBO operateDataList = getAddDataList(dataMap, cacheNeedMapping, defaultValueMap);
                sqlExecuteMapper.executeInsert(serviceInfo.getInsertTable(), operateDataList);
                // 返回新增后的主键
                Integer id = operateDataList.getId();
                yield Objects.nonNull(id) ? id : 1;
            }
            case UPDATE -> {
                // 过滤不需要操作的列配置项
                Map<String, ServiceCol> cacheNeedMapping = serviceColList.stream().filter(serviceCol -> (serviceCol.getAllowUpdate() != 0 || serviceCol.getIsWhereKey() == 1))
                        .collect(Collectors.toMap(ServiceCol::getJsonCol, serviceCol -> serviceCol, (k1, k2) -> k1));
                yield sqlExecuteMapper.executeUpdate(serviceInfo.getUpdateTable(), getEditDataList(dataMap, cacheNeedMapping));
            }
            case DELETE -> {
                // 过滤不需要操作的列配置项，并将需要过滤的列配置项转为 Map
                Map<String, ServiceCol> cacheNeedMapping = serviceColList.stream().filter(serviceCol -> (serviceCol.getIsWhereKey() == 1))
                        .collect(Collectors.toMap(ServiceCol::getJsonCol, serviceCol -> serviceCol, (k1, k2) -> k1));
                yield sqlExecuteMapper.executeDelete(serviceInfo.getDeleteTable(), getRemoveDataList(dataMap, cacheNeedMapping));
            }
            default -> throw new ServerException("不支持的操作类型");
        };
    }

    /**
     * 处理多笔数据的增删改
     *
     * @param operateType     操作类型
     * @param serviceInfo     服务信息
     * @param serviceColList  类配置信息
     * @param dataMapList     数据列表
     * @param defaultValueMap 默认值
     * @return 影响行数
     */
    private Integer processMultipleData(String operateType, ServiceInfo serviceInfo, List<ServiceCol> serviceColList, List<Map<String, Object>> dataMapList, Map<String, String> defaultValueMap) {
        return switch (operateType) {
            case INSERT -> {
                // 过滤不需要操作的列配置项
                Map<String, ServiceCol> cacheNeedMapping = serviceColList.stream().filter(serviceCol -> (serviceCol.getAllowInsert() != 0))
                        .collect(Collectors.toMap(ServiceCol::getJsonCol, serviceCol -> serviceCol, (k1, k2) -> k1));
                List<BatchOperateBO> operateBatchDataList = getAddBatchDataList(dataMapList, cacheNeedMapping, defaultValueMap);
                sqlExecuteMapper.executeInsertBatch(serviceInfo.getInsertTable(), operateBatchDataList);
                yield operateBatchDataList.size();
            }
            case UPDATE -> {
                // 过滤不需要操作的列配置项
                Map<String, ServiceCol> cacheNeedMapping = serviceColList.stream().filter(serviceCol -> (serviceCol.getAllowUpdate() != 0 || serviceCol.getIsWhereKey() == 1))
                        .collect(Collectors.toMap(ServiceCol::getJsonCol, serviceCol -> serviceCol, (k1, k2) -> k1));
                List<BatchOperateBO> operateBatchDataList = getEditBatchDataList(dataMapList, cacheNeedMapping);
                sqlExecuteMapper.executeUpdateBatch(serviceInfo.getUpdateTable(), operateBatchDataList);
                yield operateBatchDataList.size();
            }
            case DELETE -> {
                // 过滤不需要操作的列配置项，并将需要过滤的列配置项转为 Map
                Map<String, ServiceCol> cacheNeedMapping = serviceColList.stream().filter(serviceCol -> (serviceCol.getIsWhereKey() == 1))
                        .collect(Collectors.toMap(ServiceCol::getJsonCol, serviceCol -> serviceCol, (k1, k2) -> k1));
                List<BatchOperateBO> deleteBatchDataList = getRemoveBatchDataList(dataMapList, cacheNeedMapping);
                sqlExecuteMapper.executeDeleteBatch(serviceInfo.getDeleteTable(), deleteBatchDataList);
                yield deleteBatchDataList.size();
            }
            default -> throw new ServerException("不支持的操作类型");
        };
    }

    /**
     * 获取新增操作的一笔数据
     *
     * @param dataMap          数据
     * @param cacheNeedMapping 缓存可以操作的列配置项
     * @param defaultValueMap  默认值
     * @return BatchOperateBO 数据
     */
    private BatchOperateBO getAddDataList(Map<String, Object> dataMap, Map<String, ServiceCol> cacheNeedMapping, Map<String, String> defaultValueMap) {
        List<ColumnBO> columnBOList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            if (!cacheNeedMapping.containsKey(entry.getKey())) {
                continue;
            }
            Object value = entry.getValue();
            if (value instanceof String[] stringArray && stringArray.length > 0) {
                // 转 JSON 字符串
                value = JacksonUtil.toJsonStr(stringArray);
            }
            // 值不能为空
            if (value instanceof String v ? StringUtil.hasText(v) : Objects.nonNull(value)) {
                ColumnBO columnBO = new ColumnBO();
                ServiceCol serviceCol = cacheNeedMapping.get(entry.getKey());
                columnBOList.add(columnBO.setColumn(serviceCol.getTableCol()).setValue(value));
            }
        }

        // 如果不存在 key，且操作为 Insert，添加默认值
        defaultValueMap.forEach((key, value) -> {
            ColumnBO columnBO = new ColumnBO().setColumn(key).setValue(value);
            if (!columnBOList.contains(columnBO)) {
                columnBOList.add(columnBO);
            }
        });
        return new BatchOperateBO().setColumnList(columnBOList);
    }

    /**
     * 获取新增操作的多笔数据
     *
     * @param dataListMap      数据列表
     * @param cacheNeedMapping 缓存可以操作的列配置项
     * @param defaultValueMap  默认值
     * @return BatchOperateBO
     */
    private List<BatchOperateBO> getAddBatchDataList(List<Map<String, Object>> dataListMap, Map<String, ServiceCol> cacheNeedMapping, Map<String, String> defaultValueMap) {
        // 组装操作的数据
        List<BatchOperateBO> batchOperateBOList = new ArrayList<>(dataListMap.size());

        for (Map<String, Object> dataMap : dataListMap) {
            BatchOperateBO batchOperateBO = getAddDataList(dataMap, cacheNeedMapping, defaultValueMap);
            batchOperateBOList.add(batchOperateBO);
        }
        return batchOperateBOList;
    }

    /**
     * 获取修改操作的一笔数据
     *
     * @param dataMap          数据
     * @param cacheNeedMapping 缓存可以操作的列配置项
     * @return BatchOperateBO 数据
     */
    private BatchOperateBO getEditDataList(Map<String, Object> dataMap, Map<String, ServiceCol> cacheNeedMapping) {
        List<ColumnBO> columnBOList = new ArrayList<>();
        List<WhereBO> whereBOList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            if (!cacheNeedMapping.containsKey(entry.getKey())) {
                continue;
            }
            ServiceCol serviceCol = cacheNeedMapping.get(entry.getKey());
            Object value = entry.getValue();
            if (value instanceof String[] stringArray && stringArray.length > 0) {
                // 转 JSON 字符串
                value = JacksonUtil.toJsonStr(stringArray);
            }
            // 值不能为空
            if (value instanceof String v ? StringUtil.hasText(v) : Objects.nonNull(value)) {
                ColumnBO columnBO = new ColumnBO();
                WhereBO whereBO = new WhereBO();
                if (serviceCol.getIsWhereKey() == 1) {
                    whereBOList.add(whereBO.setKey(serviceCol.getTableCol()).setValue(value));
                } else if (serviceCol.getIsWhereKey() == 0) {
                    // 不是 where 条件，那么就是需要操作的数据
                    columnBOList.add(columnBO.setColumn(serviceCol.getTableCol()).setValue(value));
                }
            }
        }
        if (whereBOList.isEmpty()) {
            throw new ServerException("where 条件不能为空");
        }
        return new BatchOperateBO().setColumnList(columnBOList).setWhereList(whereBOList);
    }

    /**
     * 获取修改操作的多笔数据
     *
     * @param dataListMap      数据列表
     * @param cacheNeedMapping 缓存可以操作的列配置项
     * @return BatchOperateBO
     */
    private List<BatchOperateBO> getEditBatchDataList(List<Map<String, Object>> dataListMap, Map<String, ServiceCol> cacheNeedMapping) {
        // 组装操作的数据
        List<BatchOperateBO> batchOperateBOList = new ArrayList<>(dataListMap.size());

        for (Map<String, Object> dataMap : dataListMap) {
            BatchOperateBO batchOperateBO = getEditDataList(dataMap, cacheNeedMapping);
            batchOperateBOList.add(batchOperateBO);
        }
        return batchOperateBOList;
    }

    /**
     * 获取删除操作的一笔数据
     *
     * @param dataMap          数据
     * @param cacheNeedMapping 缓存可以操作的列配置项
     * @return BatchOperateBO 数据
     */
    private BatchOperateBO getRemoveDataList(Map<String, Object> dataMap, Map<String, ServiceCol> cacheNeedMapping) {
        List<WhereBO> whereBOList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            if (!cacheNeedMapping.containsKey(entry.getKey())) {
                continue;
            }
            ServiceCol serviceCol = cacheNeedMapping.get(entry.getKey());
            Object value = entry.getValue();
            // 值不能为空
            if (value instanceof String v ? StringUtil.hasText(v) : Objects.nonNull(value)) {
                WhereBO whereBO = new WhereBO();
                if (serviceCol.getIsWhereKey() == 1) {
                    whereBOList.add(whereBO.setKey(serviceCol.getTableCol()).setValue(value));
                }
            }
        }
        if (whereBOList.isEmpty()) {
            throw new ServerException("where 条件不能为空");
        }
        return new BatchOperateBO().setWhereList(whereBOList);
    }

    /**
     * 获取删除操作的多笔数据
     *
     * @param dataListMap      数据列表
     * @param cacheNeedMapping 缓存可以操作的列配置项
     * @return BatchOperateBO
     */
    private List<BatchOperateBO> getRemoveBatchDataList(List<Map<String, Object>> dataListMap, Map<String, ServiceCol> cacheNeedMapping) {
        // 组装操作的数据
        List<BatchOperateBO> batchOperateBOList = new ArrayList<>(dataListMap.size());

        for (Map<String, Object> dataMap : dataListMap) {
            BatchOperateBO deleteDataList = getRemoveDataList(dataMap, cacheNeedMapping);
            batchOperateBOList.add(deleteDataList);
        }
        return batchOperateBOList;
    }

}
