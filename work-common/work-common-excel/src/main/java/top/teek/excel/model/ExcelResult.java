package top.teek.excel.model;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/6/10 18:09:15
 * @note Excel 返回对象
 */
public interface ExcelResult<T> {

    /**
     * 对象列表
     */
    List<T> getList();

    /**
     * 错误列表
     */
    List<String> getErrorList();

    /**
     * 导入结果分析
     */
    String getAnalysisResult();
}
