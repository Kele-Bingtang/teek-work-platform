package top.teek.integrate.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2024-10-28 21:11:03
 * @note
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "系统整合服务日志表")
public class ConfigLogDTO {
    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "配置信息的 id")
    private Integer configId;

    @Schema(description = "异常信息")
    private String exceptionInfo;

    @Schema(description = "异常接口地址")
    private String exceptionUrl;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}