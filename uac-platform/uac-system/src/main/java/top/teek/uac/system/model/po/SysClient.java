package top.teek.uac.system.model.po;

import top.teek.mp.base.BaseDO;
import top.teek.uac.system.model.vo.SysClientVO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Teeker
 * @date 2023-58-13 22:58:55
 * @note 客户端授权
*/
@EqualsAndHashCode(callSuper = true)
@TableName(value ="t_sys_client")
@Data
@AutoMapper(target = SysClientVO.class)
public class SysClient extends BaseDO {
    /**
     * 客户端 ID
     */
    private String clientId;

    /**
     * 客户端 Key
     */
    private String clientKey;

    /**
     * 客户端名称
     */
    private String clientName;

    /**
     * 客户端秘钥
     */
    private String clientSecret;

    /**
     * 授权类型
     */
    private String grantTypes;

    /**
     * token 最低活跃频率时间，超出则 token 失效（-1 不限制，单位秒）
     */
    private Long activeTimeout;

    /**
     * token 有效期，超出则 token 失效，默认 12 小时（单位秒）
     */
    private Long timeout;
}