package top.teek.ag.system.model.po;

import top.teek.ag.system.model.vo.ProjectMemberVO;
import top.teek.mp.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Teeker
 * @date 2024-06-23 01:07:11
 * @note 项目成员表
*/
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_project_member")
@Data
@AutoMapper(target = ProjectMemberVO.class, reverseConvertGenerate = false)
public class ProjectMember extends BaseDO {
    /**
     * 用户名
     */
    private String userId;

    /**
     * 项目 ID
     */
    private Long projectId;

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
}