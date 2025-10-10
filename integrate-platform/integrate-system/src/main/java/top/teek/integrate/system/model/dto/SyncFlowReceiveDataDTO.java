package top.teek.integrate.system.model.dto;

import top.teek.integrate.system.model.bo.SyncFlowBO;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * @author Teeker
 * @date 2024/10/28 00:50:31
 * @note
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@AutoMapper(target = SyncFlowBO.class, reverseConvertGenerate = false)
public class SyncFlowReceiveDataDTO {
    
    @NotNull(message = "系统 ID 不能为空")
    @Schema(description = "Mapping ID")
    private Integer id;
    
    @Schema(description = "Source 系统数据")
    private Object sourceData;
    
    @Schema(description = "Target 系统认证参数")
    private Map<String, Object> targetAuthInfo;
    
    @Schema(description = "Target 系统请求参数")
    private Map<String, Object> targetDeliverInfo;
}