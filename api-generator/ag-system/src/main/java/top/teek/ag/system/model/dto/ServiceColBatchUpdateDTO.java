package top.teek.ag.system.model.dto;

import top.teek.core.validate.validator.ValidList;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Teeker
 * @date 2024/7/7 16:07:23
 * @note
 */
@Data
public class ServiceColBatchUpdateDTO {
    /**
     * 是否允许插入，0 不允许，1 允许
     */
    private Integer allowInsert;
    /**
     * 是否允许更新，0 不允许，1 允许
     */
    private Integer allowUpdate;
    /**
     * 是否允许返回在请求里，0 不允许，1 允许
     */
    private Integer allowRequest;
    /**
     * 请求名称集合
     */
    @NotNull(message = "批量修改的字段不能为空")
    private ValidList<String> jsonColList;

    @NotBlank(message = "项目 ID 不能为空")
    private String projectId;
}
