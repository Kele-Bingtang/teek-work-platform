package top.teek.ag.system.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Teeker
 * @date 2024/6/23 00:12:06
 * @note
 */
@Data
public class TeamMemberVO implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 团队内昵称
     */
    private String nickname;

    /**
     * 团队角色（1 所有者 2 管理员 3 普通成员）
     */
    private Integer teamRole;

    /**
     * 1 团队创建者 2 团队加入者
     */
    private Integer belongType;

    /**
     * 状态（0 异常 1 正常）
     */
    private Integer status;

    /**
     * 团队 ID
     */
    private String teamId;

    @Serial
    private static final long serialVersionUID = 1L;
}
