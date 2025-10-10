package top.teek.ag.system.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Teeker
 * @date 2024/7/3 00:02:40
 * @note
 */
@Data
public class SqlDTO {
    /**
     * 数据源 ID
     */
    @NotBlank(message = "数据源 ID 不能为空")
    private String dataSourceId;

    /**
     * Schema
     */
    private String schema;
    
    /**
     * SQL 语句
     */
    @NotBlank(message = "SQL 语句不能为空")
    private String sql;
}
