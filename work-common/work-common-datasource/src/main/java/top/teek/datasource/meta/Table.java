package top.teek.datasource.meta;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Teeker
 * @date 2024/6/30 17:54:00
 * @note
 */
@Data
public class Table implements Serializable {

    /**
     * 数据源 ID
     */
    private String dataSourceId;
    /**
     * 库
     */
    private String catalog;
    /**
     * 模式
     */
    private String schema;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 表类型。典型的类型是 TABLE，VIEW，SYSTEM TABLE，GLOBAL TEMPORARY，LOCAL TEMPORARY，ALIAS，SYNONYM。
     */
    private String tableType;
    /**
     * 表备注
     */
    private String remarks;
    /**
     * 表状态
     */
    private String status;
    /**
     * 表大小（字节单位）
     */
    private Long size;
    /**
     * 表行数
     */
    private Long rows;
    /**
     * 表创建时间
     */
    private Date createTime;
    /**
     * 表修改时间
     */
    private Date updateTime;
    
    @Serial
    private static final long serialVersionUID = 1L;
}
