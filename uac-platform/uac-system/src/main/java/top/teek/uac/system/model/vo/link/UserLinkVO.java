package top.teek.uac.system.model.vo.link;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2024/3/19 22:20
 * @note 用户被关联数据，如被用户组关联、被角色关联
 */
@Data
public class UserLinkVO {

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 应用 ID
     */
    private String appId;
}
