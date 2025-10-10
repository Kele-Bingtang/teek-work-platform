package top.teek.uac.system.model.dto;

import top.teek.core.validate.RestGroup;
import top.teek.uac.system.model.po.SysClient;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author Teeker
 * @date 2023/11/26 22:55
 * @note
 */
@Data
@AutoMapper(target = SysClient.class, reverseConvertGenerate = false)
public class SysClientDTO {

    /**
     * id
     */
    @NotNull(message = "id 不能为空", groups = {RestGroup.EditGroup.class, RestGroup.DeleteGroup.class})
    public Long id;

    /**
     * 客户端 ID
     */
    private String clientId;

    /**
     * 客户端 Key
     */
    @NotBlank(message = "客户端 key 不能为空", groups = {RestGroup.AddGroup.class})
    private String clientKey;

    /**
     * 客户端名称
     */
    @NotBlank(message = "客户端名称 不能为空", groups = {RestGroup.AddGroup.class})
    private String clientName;

    /**
     * 客户端秘钥
     */
    private String clientSecret;

    /**
     * 授权类型
     */
    @NotNull(message = "授权类型不能为空", groups = {RestGroup.AddGroup.class})
    private List<String> grantTypeList;

    /**
     * token 最低活跃频率时间，超出则 token 失效（-1 不限制，单位秒）
     */
    private Long activeTimeout;

    /**
     * token 有效期，超出则 token 失效，默认 12 小时（单位秒）
     */
    private Long timeout;

    /**
     * 状态（0 异用 1 正常）
     */
    private Integer status;
}
