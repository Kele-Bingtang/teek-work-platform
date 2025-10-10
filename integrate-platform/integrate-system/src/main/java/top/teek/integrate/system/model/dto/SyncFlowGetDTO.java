package top.teek.integrate.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Teeker
 * @date 2024-10-28 21:11:50
 * @note 
*/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SyncFlowGetDTO {
    @NotNull(message = "系统 ID 不能为空")
    @Schema(description = "Mapping ID")
    private Integer id;
    
    @Schema(description = "Source 系统认证参数")
    private List<String> sourceAuthInfo;
    
    @Schema(description = "Target 系统认证参数")
    private List<String> targetAuthInfo;
    
    @Schema(description = "Source 系统请求参数")
    private List<String> sourceDeliverInfo;
    
    @Schema(description = "Target 系统请求参数")
    private List<String> targetDeliverInfo;
}