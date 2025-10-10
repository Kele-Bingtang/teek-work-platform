package top.teek.uac.system.model.vo.link;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2024/3/16 2:12
 * @note 用户组被关联数据，如被用户关联、被角色关联
 */
@Data
public class UserGroupLinkVO {

    /**
     * 用户组名
     */
    private String groupName;

    /**
     * 用户组描述
     */
    private String intro;

    /**
     * 负责人 ID
     */
    private String ownerId;

    /**
     * 负责人 username
     */
    private String ownerName;

    /**
     * 关联 ID
     */
    private Long linkId;

    /**
     * 生效时间
     */
    private LocalDate validFrom;

    /**
     * 失效时间
     */
    private LocalDate expireOn;

    /**
     * 应用 ID
     */
    private String appId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
