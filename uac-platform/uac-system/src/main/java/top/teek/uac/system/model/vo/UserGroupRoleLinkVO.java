package top.teek.uac.system.model.vo;

import lombok.Data;

/**
 * @author Teeker
 * @date 2023/12/28 0:03
 * @note 目前接口是批量关联（多个 ids），该类是一对一关联，因此暂未使用
 * 批量关联请看 link 目录下的 VO 类
 */
@Data
public class UserGroupRoleLinkVO {
    /**
     * 用户组 ID
     */
    private String userGroupId;

    /**
     * 角色 ID
     */
    private String roleId;

    /**
     * 应用 ID
     */
    private String appId;
}
