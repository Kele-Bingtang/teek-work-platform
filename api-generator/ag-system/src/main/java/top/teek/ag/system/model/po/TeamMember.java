package top.teek.ag.system.model.po;

import top.teek.ag.system.model.vo.TeamMemberVO;
import top.teek.mp.annotation.FieldValueFill;
import top.teek.mp.annotation.ValueStrategy;
import top.teek.mp.base.BaseDO;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Teeker
 * @date 2024-04-23 01:04:43
 * @note 团队成员
*/
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_team_member")
@Data
@AutoMapper(target = TeamMemberVO.class, reverseConvertGenerate = false)
public class TeamMember extends BaseDO {

    /**
     * 用户 ID
     */
    @TableField(fill = FieldFill.INSERT)
    @FieldValueFill(ValueStrategy.SNOWFLAKE)
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
     * 团队 ID
     */
    private String teamId;
}