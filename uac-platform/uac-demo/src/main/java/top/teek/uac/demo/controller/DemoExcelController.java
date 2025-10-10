package top.teek.uac.demo.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import top.teek.uac.demo.listen.ExportDemoListener;
import top.teek.uac.demo.model.ExportDemoVO;
import top.teek.excel.dropdown.DropDownOptions;
import top.teek.excel.helper.ExcelHelper;
import top.teek.excel.model.ExcelResult;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Teeker
 * @date 2024/6/10 19:41:38
 * @note
 */
@RestController
@RequestMapping("/demo/excel")
public class DemoExcelController {
    /**
     * 导出纯模板
     */
    @GetMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        ExcelHelper.exportTemplate("单列表.xlsx", "excel/单列表.xlsx", response);
    }

    /**
     * 单列表多数据
     */
    @GetMapping("/exportTemplateOne")
    public void exportTemplateOne(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();
        map.put("title", "单列表多数据");
        map.put("test1", "数据测试1");
        map.put("test2", "数据测试2");
        map.put("test3", "数据测试3");
        map.put("test4", "数据测试4");
        map.put("testTest", "666");
        List<TestObj> list = new ArrayList<>();
        list.add(new TestObj("单列表测试1", "列表测试1", "列表测试2", "列表测试3", "列表测试4"));
        list.add(new TestObj("单列表测试2", "列表测试5", "列表测试6", "列表测试7", "列表测试8"));
        list.add(new TestObj("单列表测试3", "列表测试9", "列表测试10", "列表测试11", "列表测试12"));
        ExcelHelper.exportTemplate(CollUtil.newArrayList(map, list), "单列表.xlsx", "excel/单列表.xlsx", response);
    }

    /**
     * 多列表多数据
     */
    @GetMapping("/exportTemplateMulti")
    public void exportTemplateMulti(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();
        map.put("title1", "标题1");
        map.put("title2", "标题2");
        map.put("title3", "标题3");
        map.put("title4", "标题4");
        map.put("author", "Lion Li");
        List<TestObj1> list1 = new ArrayList<>();
        list1.add(new TestObj1("list1测试1", "list1测试2", "list1测试3"));
        list1.add(new TestObj1("list1测试4", "list1测试5", "list1测试6"));
        list1.add(new TestObj1("list1测试7", "list1测试8", "list1测试9"));
        List<TestObj1> list2 = new ArrayList<>();
        list2.add(new TestObj1("list2测试1", "list2测试2", "list2测试3"));
        list2.add(new TestObj1("list2测试4", "list2测试5", "list2测试6"));
        List<TestObj1> list3 = new ArrayList<>();
        list3.add(new TestObj1("list3测试1", "list3测试2", "list3测试3"));
        List<TestObj1> list4 = new ArrayList<>();
        list4.add(new TestObj1("list4测试1", "list4测试2", "list4测试3"));
        list4.add(new TestObj1("list4测试4", "list4测试5", "list4测试6"));
        list4.add(new TestObj1("list4测试7", "list4测试8", "list4测试9"));
        list4.add(new TestObj1("list4测试10", "list4测试11", "list4测试12"));
        Map<String, Object> multiListMap = new HashMap<>();
        multiListMap.put("map", map);
        multiListMap.put("data1", list1);
        multiListMap.put("data2", list2);
        multiListMap.put("data3", list3);
        multiListMap.put("data4", list4);
        ExcelHelper.exportTemplateMultiList(multiListMap, "多列表.xlsx", "excel/多列表.xlsx", response);
    }

    /**
     * 导出下拉框
     */
    @GetMapping("/exportWithOptions")
    public void exportWithOptions(HttpServletResponse response) {
        exportOptions(response);
    }

    /**
     * 多个 sheet 导出
     */
    @GetMapping("/exportTemplateMultiSheet")
    public void exportTemplateMultiSheet(HttpServletResponse response) {
        List<TestObj1> list1 = new ArrayList<>();
        list1.add(new TestObj1("list1测试1", "list1测试2", "list1测试3"));
        list1.add(new TestObj1("list1测试4", "list1测试5", "list1测试6"));
        List<TestObj1> list2 = new ArrayList<>();
        list2.add(new TestObj1("list2测试1", "list2测试2", "list2测试3"));
        list2.add(new TestObj1("list2测试4", "list2测试5", "list2测试6"));
        List<TestObj1> list3 = new ArrayList<>();
        list3.add(new TestObj1("list3测试1", "list3测试2", "list3测试3"));
        list3.add(new TestObj1("list3测试4", "list3测试5", "list3测试6"));
        List<TestObj1> list4 = new ArrayList<>();
        list4.add(new TestObj1("list4测试1", "list4测试2", "list4测试3"));
        list4.add(new TestObj1("list4测试4", "list4测试5", "list4测试6"));

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> sheetMap1 = new HashMap<>();
        sheetMap1.put("data1", list1);
        Map<String, Object> sheetMap2 = new HashMap<>();
        sheetMap2.put("data2", list2);
        Map<String, Object> sheetMap3 = new HashMap<>();
        sheetMap3.put("data3", list3);
        Map<String, Object> sheetMap4 = new HashMap<>();
        sheetMap4.put("data4", list4);

        list.add(sheetMap1);
        list.add(sheetMap2);
        list.add(sheetMap3);
        list.add(sheetMap4);
        ExcelHelper.exportTemplateMultiSheet(list, "多sheet列表", "excel/多sheet列表.xlsx", response);
    }

    /**
     * 导入表格
     */
    @PostMapping(value = "/importWithOptions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<ExportDemoVO> importWithOptions(@RequestPart("file") MultipartFile file) throws Exception {
        // 处理解析结果
        ExcelResult<ExportDemoVO> excelResult = ExcelHelper.importExcel(file.getInputStream(), ExportDemoVO.class, new ExportDemoListener());
        return excelResult.getList();
    }

    @Data
    @AllArgsConstructor
    static class TestObj1 {
        private String test1;
        private String test2;
        private String test3;
    }

    @Data
    @AllArgsConstructor
    static class TestObj {
        private String name;
        private String list1;
        private String list2;
        private String list3;
        private String list4;
    }


    private void exportOptions(HttpServletResponse response) {
        // 创建表格数据，业务中一般通过数据库查询
        List<ExportDemoVO> excelDataList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            // 模拟数据库中的一条数据
            ExportDemoVO everyRowData = new ExportDemoVO();
            everyRowData.setNickName("用户-" + i);
            everyRowData.setUserStatus(ExportDemoVO.UserStatus.OK.getValue());
            everyRowData.setGender("1");
            everyRowData.setPhoneNumber(String.format("175%08d", i));
            everyRowData.setEmail(String.format("175%08d", i) + "@163.com");
            everyRowData.setProvinceId(i);
            everyRowData.setCityId(i);
            everyRowData.setAreaId(i);
            excelDataList.add(everyRowData);
        }

        // 通过 @ExcelIgnoreUnannotated 配合 @ExcelProperty 合理显示需要的列
        // 并通过 @DropDown 注解指定下拉值，或者通过创建 ExcelOptions 来指定下拉框
        // 使用 ExcelOptions 时建议指定列 index，防止出现下拉列解析不对齐

        // 首先从数据库中查询下拉框内的可选项
        // 这里模拟查询结果
        List<DemoCityData> provinceList = getProvinceList(),
                cityList = getCityList(provinceList),
                areaList = getAreaList(cityList);
        int provinceIndex = 5, cityIndex = 6, areaIndex = 7;

        DropDownOptions provinceToCity = DropDownOptions.buildLinkedOptions(
                provinceList,
                provinceIndex,
                cityList,
                cityIndex,
                DemoCityData::getId,
                DemoCityData::getPid,
                everyOptions -> DropDownOptions.createOptionValue(
                        everyOptions.getName(),
                        everyOptions.getId()
                )
        );

        DropDownOptions cityToArea = DropDownOptions.buildLinkedOptions(
                cityList,
                cityIndex,
                areaList,
                areaIndex,
                DemoCityData::getId,
                DemoCityData::getPid,
                everyOptions -> DropDownOptions.createOptionValue(
                        everyOptions.getName(),
                        everyOptions.getId()
                )
        );

        // 把所有的下拉框存储
        List<DropDownOptions> options = new ArrayList<>();
        options.add(provinceToCity);
        options.add(cityToArea);

        // 到此为止所有的下拉框可选项已全部配置完毕

        // 接下来需要将 Excel 中的展示数据转换为对应的下拉选
        List<ExportDemoVO> outList = excelDataList.stream().map(everyRowData -> {
            // 只需要处理没有使用 @ExcelDictFormat 注解的下拉框
            // 一般来说，可以直接在数据库查询即查询出省市县信息，这里通过模拟操作赋值
            everyRowData.setProvince(buildOptions(provinceList, everyRowData.getProvinceId()));
            everyRowData.setCity(buildOptions(cityList, everyRowData.getCityId()));
            everyRowData.setArea(buildOptions(areaList, everyRowData.getAreaId()));
            return everyRowData;
        }).toList();

        ExcelHelper.exportExcel(outList, "下拉框示例", ExportDemoVO.class, response, options);
    }

    private String buildOptions(List<DemoCityData> cityDataList, Integer id) {
        Map<Integer, List<DemoCityData>> groupByIdMap =
                cityDataList.stream().collect(Collectors.groupingBy(DemoCityData::getId));
        if (groupByIdMap.containsKey(id)) {
            DemoCityData demoCityData = groupByIdMap.get(id).get(0);
            return DropDownOptions.createOptionValue(demoCityData.getName(), demoCityData.getId());
        } else {
            return StrUtil.EMPTY;
        }
    }

    /**
     * 模拟查询数据库操作
     *
     * @return /
     */
    private List<DemoCityData> getProvinceList() {
        List<DemoCityData> provinceList = new ArrayList<>();

        // 实际业务中一般采用数据库读取的形式，这里直接拼接创建
        provinceList.add(new DemoCityData(0, null, "安徽省"));
        provinceList.add(new DemoCityData(1, null, "江苏省"));

        return provinceList;
    }

    /**
     * 模拟查找数据库操作，需要连带查询出省的数据
     *
     * @param provinceList 模拟的父省数据
     * @return /
     */
    private List<DemoCityData> getCityList(List<DemoCityData> provinceList) {
        List<DemoCityData> cityList = new ArrayList<>();

        // 实际业务中一般采用数据库读取的形式，这里直接拼接创建
        cityList.add(new DemoCityData(0, 0, "合肥市"));
        cityList.add(new DemoCityData(1, 0, "芜湖市"));
        cityList.add(new DemoCityData(2, 1, "南京市"));
        cityList.add(new DemoCityData(3, 1, "无锡市"));
        cityList.add(new DemoCityData(4, 1, "徐州市"));

        selectParentData(provinceList, cityList);

        return cityList;
    }

    /**
     * 模拟查找数据库操作，需要连带查询出市的数据
     *
     * @param cityList 模拟的父市数据
     * @return /
     */
    private List<DemoCityData> getAreaList(List<DemoCityData> cityList) {
        List<DemoCityData> areaList = new ArrayList<>();

        // 实际业务中一般采用数据库读取的形式，这里直接拼接创建
        areaList.add(new DemoCityData(0, 0, "瑶海区"));
        areaList.add(new DemoCityData(1, 0, "庐江区"));
        areaList.add(new DemoCityData(2, 1, "南宁县"));
        areaList.add(new DemoCityData(3, 1, "镜湖区"));
        areaList.add(new DemoCityData(4, 2, "玄武区"));
        areaList.add(new DemoCityData(5, 2, "秦淮区"));
        areaList.add(new DemoCityData(6, 3, "宜兴市"));
        areaList.add(new DemoCityData(7, 3, "新吴区"));
        areaList.add(new DemoCityData(8, 4, "鼓楼区"));
        areaList.add(new DemoCityData(9, 4, "丰县"));

        selectParentData(cityList, areaList);

        return areaList;
    }

    /**
     * 模拟数据库的查询父数据操作
     *
     * @param parentList /
     * @param sonList    /
     */
    private void selectParentData(List<DemoCityData> parentList, List<DemoCityData> sonList) {
        Map<Integer, List<DemoCityData>> parentGroupByIdMap =
                parentList.stream().collect(Collectors.groupingBy(DemoCityData::getId));

        sonList.forEach(everySon -> {
            if (parentGroupByIdMap.containsKey(everySon.getPid())) {
                everySon.setPData(parentGroupByIdMap.get(everySon.getPid()).get(0));
            }
        });
    }

    /**
     * 模拟的数据库省市县
     */
    @Data
    private static class DemoCityData {
        /**
         * 数据库id字段
         */
        private Integer id;
        /**
         * 数据库pid字段
         */
        private Integer pid;
        /**
         * 数据库name字段
         */
        private String name;
        /**
         * MyBatisPlus连带查询父数据
         */
        private DemoCityData pData;

        public DemoCityData(Integer id, Integer pid, String name) {
            this.id = id;
            this.pid = pid;
            this.name = name;
        }
    }
}
