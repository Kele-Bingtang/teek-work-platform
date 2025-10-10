package top.teek.auth.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * @author Teeker
 * @date 2023-41-24 02:41:30
 * @note 
*/
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenApiClientDTO {
    @NotBlank(message = "客户名称不能为空")
    private String appName;
    @NotBlank(message = "客户编码不能为空")
    private String appCode;
}