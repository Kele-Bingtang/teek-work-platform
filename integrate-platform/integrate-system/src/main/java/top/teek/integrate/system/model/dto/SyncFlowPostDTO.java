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

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@AutoMapper(target = SyncFlowBO.class, reverseConvertGenerate = false)
public class SyncFlowPostDTO {
    
    @NotNull(message = "系统 ID 不能为空")
    @Schema(description = "Mapping ID")

    private Integer id;
    @Schema(description = "Source 系统认证参数")
    private Map<String, Object> sourceAuthInfo;

    @Schema(description = "Target 系统认证参数")
    private Map<String, Object> targetAuthInfo;

    @Schema(description = "Source 系统请求参数")
    private Map<String, Object> sourceDeliverInfo;

    @Schema(description = "Target 系统请求参数")
    private Map<String, Object> targetDeliverInfo;
}