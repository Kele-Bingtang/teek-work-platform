package top.teek.ag.system.model.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/7/11 01:19:35
 * @note
 */
@Data
@Accessors(chain = true)
public class BatchOperateBO {
    private Integer id;
    private List<ColumnBO> columnList;
    private List<WhereBO> whereList;
}
