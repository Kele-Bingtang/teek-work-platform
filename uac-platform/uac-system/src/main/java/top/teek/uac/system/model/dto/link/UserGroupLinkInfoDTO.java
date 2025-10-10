package top.teek.uac.system.model.dto.link;

import lombok.Data;

/**
 * @author Teeker
 * @date 2024/4/2 23:49
 * @note
 */
@Data
public class UserGroupLinkInfoDTO {
    /**
     * 用户组名
     */
    private String userGroupName;

    /**
     * 负责人

     */
    private String owner;
}
