package top.teek.uac.auth.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/1/16 0:34
 * @note 租户下拉选择列表
 */
@Data
@Builder
public class LoginTenantSelectVO {
    private boolean tenantEnabled;
    private List<TenantSelectVO> tenantSelectList;
}