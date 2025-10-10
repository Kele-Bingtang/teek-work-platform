package top.teek.uac.demo.listen;

import cn.hutool.core.util.NumberUtil;
import top.teek.core.validate.RestGroup;
import top.teek.uac.demo.model.ExportDemoVO;
import top.teek.excel.dropdown.DropDownOptions;
import top.teek.excel.listen.BaseExcelListener;
import top.teek.utils.ValidatorUtil;
import com.alibaba.excel.context.AnalysisContext;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/6/10 19:52:48
 * @note Excel 带下拉框的解析处理器
 */
public class ExportDemoListener extends BaseExcelListener<ExportDemoVO> {

    public ExportDemoListener() {
        // 显示使用构造函数，否则将导致空指针
        super(true);
    }

    @Override
    public void invoke(ExportDemoVO data, AnalysisContext context) {
        // 先校验必填
        ValidatorUtil.validate(data, RestGroup.AddGroup.class);

        // 处理级联下拉的部分
        String province = data.getProvince();
        String city = data.getCity();
        String area = data.getArea();
        // 本行用户选择的省
        List<String> thisRowSelectedProvinceOption = DropDownOptions.analyzeOptionValue(province);
        if (thisRowSelectedProvinceOption.size() == 2) {
            String provinceIdStr = thisRowSelectedProvinceOption.get(1);
            if (NumberUtil.isNumber(provinceIdStr)) {
                // 严格要求数据的话可以在这里做与数据库相关的判断
                // 例如判断省信息是否在数据库中存在等，建议结合RedisCache做缓存10s，减少数据库调用
                data.setProvinceId(Integer.parseInt(provinceIdStr));
            }
        }
        // 本行用户选择的市
        List<String> thisRowSelectedCityOption = DropDownOptions.analyzeOptionValue(city);
        if (thisRowSelectedCityOption.size() == 2) {
            String cityIdStr = thisRowSelectedCityOption.get(1);
            if (NumberUtil.isNumber(cityIdStr)) {
                data.setCityId(Integer.parseInt(cityIdStr));
            }
        }
        // 本行用户选择的县
        List<String> thisRowSelectedAreaOption = DropDownOptions.analyzeOptionValue(area);
        if (thisRowSelectedAreaOption.size() == 2) {
            String areaIdStr = thisRowSelectedAreaOption.get(1);
            if (NumberUtil.isNumber(areaIdStr)) {
                data.setAreaId(Integer.parseInt(areaIdStr));
            }
        }

        // 处理完毕以后判断是否符合规则
        ValidatorUtil.validate(data, RestGroup.EditGroup.class);

        // 添加到处理结果中
        getExcelResult().getList().add(data);
    }
}
