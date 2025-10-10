package top.teek.datasource.meta;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Teeker
 * @date 2024/6/30 17:53:54
 * @note
 */
@Data
public class Schema implements Serializable {
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

    @Serial
    private static final long serialVersionUID = 1L;
}
