package top.teek.ag.system.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 团队表
 */
@Data
public class TeamVO implements Serializable {
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
     * 团队介绍
     */
    private String description;
    
    /**
     * 负责人 ID
     */
    private String ownerId;

    /**
     * 负责人
     */
    private String ownerName;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 状态（0 异常 1 正常 ）
     */
    private Integer status;

    @Serial
    private static final long serialVersionUID = 1L;
}