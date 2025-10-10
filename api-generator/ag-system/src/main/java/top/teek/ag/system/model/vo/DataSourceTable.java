package top.teek.ag.system.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/7/2 23:27:22
 * @note
 */
@Data
public class DataSourceTable {
    /**
     * Table 类型，1 表，2 视图
     */
    private String tableType;
    /**
     * 表名列表或者视图列表
     */
    private List<String> tableNameList;
}
