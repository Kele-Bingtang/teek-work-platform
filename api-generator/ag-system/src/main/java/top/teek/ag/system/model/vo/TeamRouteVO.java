package top.teek.ag.system.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Teeker
 * @date 2024/6/22 15:04:14
 * @note
 */
@Data
public class TeamRouteVO implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 团队 ID
     */
    private String teamId;

    /**
     * 团队名字
     */
    private String teamName;
    
    /**
     * 团队角色（1 所有者 2 管理员 3 普通成员）
     */
    private Integer teamRole;

    /**
     * 1 团队创建者 2 团队加入者
     */
    private Integer belongType;

    @Serial
    private static final long serialVersionUID = 1L;
}
