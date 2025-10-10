package top.teek.excel.model;

import cn.hutool.core.util.StrUtil;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Teeker
 * @date 2024/6/10 18:11:11
 * @note
 */
@Setter
public class BaseExcelResult<T> implements ExcelResult<T> {

    /**
     * 数据对象 list
     */
    private List<T> list;

    /**
     * 错误信息列表
     */
    private List<String> errorList;

    public BaseExcelResult() {
        this.list = new ArrayList<>();
        this.errorList = new ArrayList<>();
    }

    public BaseExcelResult(List<T> list, List<String> errorList) {
        this.list = list;
        this.errorList = errorList;
    }

    public BaseExcelResult(ExcelResult<T> excelResult) {
        this.list = excelResult.getList();
        this.errorList = excelResult.getErrorList();
    }
    
    @Override
    public List<T> getList() {
        return list;
    }

    @Override
    public List<String> getErrorList() {
        return errorList;
    }

    @Override
    public String getAnalysisResult() {
        int successCount = list.size();
        int errorCount = errorList.size();
        if (successCount == 0) {
            return "读取失败，未解析到数据";
        } else {
            if (errorCount == 0) {
                return StrUtil.format("恭喜您，全部读取成功！共{}条", successCount);
            } else {
                return "";
            }
        }
    }
}
