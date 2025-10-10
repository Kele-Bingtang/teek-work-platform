package top.teek.uac.system.model.vo.link;

import lombok.Data;

/**
 * @author Teeker
 * @date 2024/3/14 1:38
 * @note 角色穿梭框数据，如果 disabled 为 true，则禁选
 */
@Data
public class RoleBindSelectVO {

    /**
     * 角色 ID
     */
    private String roleId;

    /**
     * 角色码
     */
    private String roleCode;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 是否禁用，给前端穿梭框使用
     */
    private Boolean disabled;
}