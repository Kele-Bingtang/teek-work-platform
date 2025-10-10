package top.teek.tenant.base;

import top.teek.mp.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Teeker
 * @date 2023/11/19 23:35
 * @note
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantBO extends BaseDO {
    /**
     * 租户编号
     */
    private String tenantId;
}
