package top.teek.ag.system.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Teeker
 * @date 2024/6/23 23:48:53
 * @note
 */
@Data
public class ProjectMemberVO implements Serializable {
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String userId;

    /**
     * 项目 ID
     */
    private String projectId;
    
    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目角色（1 管理员 2 普通成员 3 只读成员 4 禁止访问）
     */
    private Integer projectRole;

    /**
     * 0 项目创建者 1 项目加入者
     */
    private Integer belongType;

    /**
     * 团队 ID
     */
    private String teamId;

    /**
     * 创建时间
     */
    private Date createTime;

    @Serial
    private static final long serialVersionUID = 1L;
}
