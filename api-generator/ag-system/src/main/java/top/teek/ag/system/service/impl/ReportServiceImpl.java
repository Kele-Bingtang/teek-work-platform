package top.teek.ag.system.service.impl;

import top.teek.ag.core.constant.ColumnConstant;
import top.teek.ag.core.enums.DropdownConfigType;
import top.teek.ag.core.enums.QueryFilterType;
import top.teek.ag.core.helper.AgHelper;
import top.teek.ag.system.mapper.ReportMapper;
import top.teek.ag.system.mapper.ServiceColMapper;
import top.teek.ag.system.mapper.ServiceInfoMapper;
import top.teek.ag.system.mapper.base.SQLExecuteMapper;
import top.teek.ag.system.model.dto.ReportDTO;
import top.teek.ag.system.model.po.Report;
import top.teek.ag.system.model.po.ServiceCol;
import top.teek.ag.system.model.po.ServiceInfo;
import top.teek.ag.system.model.vo.ReportDataVO;
import top.teek.ag.system.model.vo.ReportVO;
import top.teek.ag.system.model.vo.component.ProFormSchemaVO;
import top.teek.ag.system.model.vo.component.ProTableColumnsVO;
import top.teek.ag.system.permission.PermissionHelper;
import top.teek.ag.system.service.ApiService;
import top.teek.ag.system.service.ReportService;
import top.teek.datasource.helper.DataSourceHelper;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Teeker
 * @date 2024-06-22 00:15:48
 * @note 针对表「t_report（报表）」的数据库操作 Service 实现
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

    private final ApiService apiService;

    private final ServiceColMapper serviceColMapper;
    private final ServiceInfoMapper serviceInfoMapper;
    private final SQLExecuteMapper sqlExecuteMapper;

    @Override
    public ReportVO getReportByServiceId(String serviceId) {
        Report report = baseMapper.selectOne(Wrappers.<Report>lambdaQuery()
                .eq(Report::getServiceId, serviceId)
        );
        PermissionHelper.checkProjectReader(AgHelper.getUserId(), report.getProjectId(), "1h");
        return MapstructUtil.convert(report, ReportVO.class);
    }

    private LambdaQueryWrapper<Report> buildQueryWrapper(ReportDTO reportDTO) {
        return Wrappers.<Report>lambdaQuery()
                .eq(StringUtil.hasText(reportDTO.getServiceId()), Report::getServiceId, reportDTO.getServiceId())
                .eq(StringUtil.hasText(reportDTO.getReportTitle()), Report::getReportTitle, reportDTO.getReportTitle())
                .orderByDesc(Report::getCreateTime);
    }

    @Override
    public boolean addReport(ReportDTO reportDTO) {
        Report report = MapstructUtil.convert(reportDTO, Report.class);
        return baseMapper.insert(report) > 0;
    }

    @Override
    public boolean editReport(ReportDTO reportDTO) {
        Report report = MapstructUtil.convert(reportDTO, Report.class);
        return baseMapper.updateById(report) > 0;
    }

    @Override
    public boolean removeReport(String serviceId) {
        return baseMapper.delete(Wrappers.<Report>lambdaQuery()
                .eq(Report::getServiceId, serviceId)) > 0;
    }

    @Override
    public boolean removeReportByCategoryId(String categoryId) {
        return baseMapper.delete(Wrappers.<Report>lambdaQuery()
                .eq(Report::getCategoryId, categoryId)) > 0;
    }

    @Override
    public boolean removeReportByProjectIdId(String projectId) {
        return baseMapper.delete(Wrappers.<Report>lambdaQuery()
                .eq(Report::getProjectId, projectId)) > 0;
    }

    @Override
    public boolean removeReportByTeamId(String teamId) {
        return baseMapper.delete(Wrappers.<Report>lambdaQuery()
                .eq(Report::getTeamId, teamId)) > 0;
    }

    @Override
    public boolean checkReportTitleUnique(ReportDTO reportDTO) {
        return baseMapper.exists(Wrappers.<Report>lambdaQuery()
                .eq(Report::getReportTitle, reportDTO.getReportTitle())
                .eq(Report::getProjectId, reportDTO.getProjectId())
                .ne(Objects.nonNull(reportDTO.getId()), Report::getId, reportDTO.getId()));
    }

    @Override
    public ReportDataVO listReportConfig(String serviceId) {
        ReportVO report = getReportByServiceId(serviceId);

        ServiceInfo serviceInfo = serviceInfoMapper.selectOne(Wrappers.<ServiceInfo>lambdaQuery()
                .eq(ServiceInfo::getServiceId, serviceId)
        );

        List<ServiceCol> serviceColList = serviceColMapper.selectList(Wrappers.<ServiceCol>lambdaQuery()
                .eq(ServiceCol::getServiceId, serviceId)
        );

        List<ProTableColumnsVO> proTableColumnsVOList = new ArrayList<>();
        List<ProFormSchemaVO> proFormSchemaVOList = new ArrayList<>();
        // 升序，值越小，优先级越高
        List<ServiceCol> sortServiceColList = serviceColList.stream().sorted(Comparator.comparingInt(ServiceCol::getDisplaySeq)
                .thenComparingInt(serviceCol -> Math.abs(serviceCol.getDisplaySeq()))).toList();

        // 是否显示行数
        if (report.getAllowRow() == 1) {
            proTableColumnsVOList.add(new ProTableColumnsVO().setType("index").setProp("_index").setLabel("#").setWidth(70));
        }

        for (ServiceCol serviceCol : sortServiceColList) {
            if (serviceCol.getAllowShowInReport() == 1) {
                proTableColumnsVOList.addAll(buildProTableColumns(serviceCol, serviceInfo, report.getAllowFilter()));
            }

            if (serviceCol.getAllowShowInDetail() == 1) {
                proFormSchemaVOList.addAll(buildProFormSchema(serviceCol, serviceInfo));
            }
        }

        return new ReportDataVO()
                .setProTableColumnsList(proTableColumnsVOList)
                .setProFormSchemaList(proFormSchemaVOList)
                .setReport(report);
    }

    /**
     * 构建表格列配置项
     *
     * @param serviceCol  服务列配置项信息
     * @param serviceInfo 服务信息
     * @param allowFilter 是否开启查询功能
     * @return 表格列配置项
     */
    private List<ProTableColumnsVO> buildProTableColumns(ServiceCol serviceCol, ServiceInfo serviceInfo, Integer allowFilter) {
        List<ProTableColumnsVO> proTableColumnsVOList = new ArrayList<>();
        ProTableColumnsVO proTableColumnsVO = new ProTableColumnsVO();
        proTableColumnsVO.setProp(serviceCol.getJsonCol());
        proTableColumnsVO.setLabel(serviceCol.getReportCol());
        proTableColumnsVO.setWidth(serviceCol.getReportColWidth() == -1 ? null : serviceCol.getReportColWidth());
        proTableColumnsVO.setAlign(serviceCol.getColAlign() == 1 ? "left" : serviceCol.getColAlign() == 2 ? "center" : "right");
        Map<String, Object> dropdownConfig = serviceCol.getDropdownConfig();
        if (Objects.nonNull(dropdownConfig) && !dropdownConfig.isEmpty()) {
            // 下拉值枚举配置（表格需要根据枚举类格式化，因此不需要开启搜索才获取枚举配置）
            proTableColumnsVO.setEnumMap(getSelectOptions(dropdownConfig, serviceInfo));
        }

        if (allowFilter == 1 && !Objects.equals(serviceCol.getQueryFilter(), QueryFilterType.NO_FILTER.getIndex())) {
            // 设置搜索的 el 类型
            ProTableColumnsVO.Search search = new ProTableColumnsVO.Search();
            if (Objects.nonNull(dropdownConfig) && !dropdownConfig.isEmpty()) {
                // 下拉值枚举 EL 配置
                proTableColumnsVO.setSearch(search.setEl("ElSelect"));
            } else if (serviceCol.getQueryFilter().equals(QueryFilterType.BETWEEN.getIndex())) {
                // 判断字段类型，觉得是日期范围还是日期时间范围
                if (serviceCol.getColType().equals("DateTime") || serviceCol.getColType().equals("Timestamp")) {
                    search.setProps(Map.of("type", "datetimerange"));
                } else if (serviceCol.getColType().equals("Date")) {
                    search.setProps(Map.of("type", "daterange"));
                }
                proTableColumnsVO.setSearch(search.setEl("ElDatePicker"));
            } else {
                // 默认为 input 框
                proTableColumnsVO.setSearch(search.setEl("ElInput"));
            }
        }
        proTableColumnsVOList.add(proTableColumnsVO);
        return proTableColumnsVOList;
    }

    /**
     * 构建表单配置项
     *
     * @param serviceCol  服务列配置项信息
     * @param serviceInfo 服务信息
     * @return 表单配置项
     */
    private List<ProFormSchemaVO> buildProFormSchema(ServiceCol serviceCol, ServiceInfo serviceInfo) {
        List<ProFormSchemaVO> proFormSchemaVOList = new ArrayList<>();

        ProFormSchemaVO proFormSchemaVO = new ProFormSchemaVO();
        proFormSchemaVO.setProp(serviceCol.getJsonCol());
        proFormSchemaVO.setLabel(serviceCol.getReportCol());
        proFormSchemaVO.setCol(serviceCol.getDetailColSpan() == -1 ? null : Map.of("span", serviceCol.getDetailColSpan()));
        proFormSchemaVO.setDefaultValue(serviceCol.getDefaultValue());

        Map<String, Object> dropdownConfig = serviceCol.getDropdownConfig();
        if (Objects.nonNull(dropdownConfig) && !dropdownConfig.isEmpty()) {
            // 下拉值枚举配置
            proFormSchemaVO.setEl("ElSelect");
            proFormSchemaVO.setEnumMap(getSelectOptions(dropdownConfig, serviceInfo));
        } else {
            String colType = serviceCol.getColType();
            // 根据字段类型设置 el
            switch (colType) {
                case "Integer", "Double", "Long" -> proFormSchemaVO.setEl("ElInputNumber");
                case "String" -> proFormSchemaVO.setEl("ElInput");
                case "Date" -> {
                    proFormSchemaVO.setEl("ElDatePicker");
                    proFormSchemaVO.setProps(Map.of("type", "date"));
                }
                case "DateTime", "Timestamp" -> {
                    proFormSchemaVO.setEl("ElDatePicker");
                    proFormSchemaVO.setProps(Map.of("type", "datetime"));
                }
                case "Blob" -> proFormSchemaVO.setEl("ElUpload");
                case "Text" -> {
                    proFormSchemaVO.setEl("ElInput");
                    proFormSchemaVO.setProps(Map.of("type", "textarea", "minRows", 2));
                }
                default -> proFormSchemaVO.setEl("ElInput");
            }
        }
        proFormSchemaVOList.add(proFormSchemaVO);

        return proFormSchemaVOList;
    }

    /**
     * 获取下拉枚举值
     *
     * @param dropdownConfig 下拉配置项
     * @param serviceInfo    服务信息
     * @return 下拉枚举值
     */
    private List<Map<String, Object>> getSelectOptions(Map<String, Object> dropdownConfig, ServiceInfo serviceInfo) {
        String type = (String) dropdownConfig.get("type");
        Object dropdownValue = dropdownConfig.get("value");
        // 本地配置
        if (Objects.equals(DropdownConfigType.LOCAL.getType(), type)) {
            // 从配置解析获取，必须是 value、label
            return (List<Map<String, Object>>) dropdownValue;
        }
        // 从其他服务获取下拉值
        if (Objects.equals(DropdownConfigType.SERVICE.getType(), type)) {
            return getSelectOptionByService((Map<String, String>) dropdownValue);
        }
        // 执行 SQL 获取下拉值
        if (Objects.equals(DropdownConfigType.SQL.getType(), type)) {
            return getSelectOptionBySql(serviceInfo, (String) dropdownValue);
        }
        return Collections.emptyList();
    }

    /**
     * 从其他服务获取下拉值
     *
     * @param serviceConfig 服务配置项
     * @return 下拉值
     */
    private List<Map<String, Object>> getSelectOptionByService(Map<String, String> serviceConfig) {
        String serviceId = serviceConfig.get("service");
        // key 是 jsonCol，value 是实际的值
        List<LinkedHashMap<String, Object>> linkedHashMapList = apiService.listByServiceId(serviceId, new HashMap<>());

        // 获取其他服务的列配置项信息，因为下拉配置项存的是列配置项的 serviceColId，所以需要根据下拉配置项的 serviceColId 获取 jsonCol
        List<ServiceCol> serviceColList = serviceColMapper.selectList(Wrappers.<ServiceCol>lambdaQuery()
                .eq(ServiceCol::getServiceId, serviceId)
        );
        String valueName = "";
        String labelName = "";
        // 根据下拉配置的 serviceColId 获取 jsonCol
        for (ServiceCol serviceCol : serviceColList) {
            if (Objects.equals(serviceCol.getColId(), serviceConfig.get(ColumnConstant.VALUE))) {
                valueName = serviceCol.getJsonCol();
            }

            if (Objects.equals(serviceCol.getColId(), serviceConfig.get(ColumnConstant.LABEL))) {
                labelName = serviceCol.getJsonCol();
            }
        }
        // 如果 valueName 或者 labelName 为空，则返回空列表
        if (StringUtil.hasAnyEmpty(valueName, labelName)) {
            return Collections.emptyList();
        }

        String finalValueName = valueName;
        String finalLabelName = labelName;
        return linkedHashMapList.stream()
                .map(originalMap -> {
                    // 过滤空值，下拉不能有空值
                    Object value = originalMap.get(finalValueName);
                    Object label = originalMap.get(finalLabelName);
                    if (Objects.isNull(value) || Objects.isNull(label)) {
                        return null;
                    }
                    return Map.of(ColumnConstant.VALUE, value, ColumnConstant.LABEL, label);
                })
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * 执行 SQL 获取下拉值
     *
     * @param serviceInfo   服务信息
     * @param sql sql
     * @return 下拉值
     */
    private List<Map<String, Object>> getSelectOptionBySql(ServiceInfo serviceInfo, String sql) {
        DataSourceHelper.use(serviceInfo.getDataSourceId());
        List<LinkedHashMap<String, Object>> linkedHashMapList = sqlExecuteMapper.executeSelectList(sql, null);
        DataSourceHelper.close();

        return linkedHashMapList.stream()
                .map(originalMap -> {
                    // 过滤空值，下拉不能有空值
                    Object value = originalMap.get(ColumnConstant.VALUE);
                    Object label = originalMap.get(ColumnConstant.LABEL);
                    if (Objects.isNull(value) || Objects.isNull(label)) {
                        return null;
                    }
                    return Map.of(ColumnConstant.VALUE, value, ColumnConstant.LABEL, label);
                })
                .filter(Objects::nonNull)
                .toList();
    }
}




