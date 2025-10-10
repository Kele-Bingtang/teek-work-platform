package top.teek.tenant.handler;

import top.teek.security.utils.SecurityUtils;
import top.teek.tenant.properties.TenantProperties;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;

import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2023/11/19 23:15
 * @note
 */
@Slf4j
@AllArgsConstructor
public class PlusTenantLineHandler implements TenantLineHandler {

    private final TenantProperties tenantProperties;

    @Override
    public Expression getTenantId() {
        String tenantId = SecurityUtils.getTenantId();
        if (!StringUtil.hasText(tenantId)) {
            log.error("无法获取有效的租户id -> Null");
            return new NullValue();
        }
        return new StringValue(tenantId);
    }

    /**
     * 获取租户字段名
     * <p>
     * 默认字段名叫: tenant_id
     *
     * @return 租户字段名
     */
    @Override
    public String getTenantIdColumn() {
        return tenantProperties.getColumn();
    }

    @Override
    public boolean ignoreTable(String tableName) {
        // 忽略指定用户对租户的数据过滤
        List<String> excludesUsername = tenantProperties.getExcludesUsername();
        if (Objects.nonNull(excludesUsername) && !excludesUsername.isEmpty()) {
            String username = SecurityUtils.getUsername();
            if (excludesUsername.contains(username)) {
                return true;
            }
        }

        String tenantId = SecurityUtils.getTenantId();
        // 判断是否有租户
        if (StringUtil.hasText(tenantId)) {
            // 不需要过滤租户的表
            List<String> excludes = tenantProperties.getExcludesTable();
            return excludes.contains(tableName);
        }
        return true;
    }
}
