package top.teek.uac.system.model.vo.link;

import lombok.Data;

/**
 * @author Teeker
 * @date 2024/3/13 1:30
 * @note 用户组穿梭框数据，如果 disabled 为 true，则禁选
 */
@Data
public class UserGroupBindSelectVO {

    /**
     * 用户组 ID
     */
    private String groupId;

    /**
     * 用户组名
     */
    private String groupName;

    /**
     * 是否禁用，给前端穿梭框使用
     */
    private Boolean disabled;
}
