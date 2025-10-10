package top.teek.tenant.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Teeker
 * @date 2023/11/19 23:14
 * @note
 */
@Data
@ConfigurationProperties(prefix = "tenant")
public class TenantProperties {
    /**
     * 是否启用
     */
    private boolean enable;

    /**
     * 租户id字段名
     */
    private String column = "tenant_id";
    
    /**
     * 排除表
     */
    private List<String> excludesTable;

    /**
     * 需要排除租户过滤的登录用户名
     */
    private List<String> excludesUsername;
}
