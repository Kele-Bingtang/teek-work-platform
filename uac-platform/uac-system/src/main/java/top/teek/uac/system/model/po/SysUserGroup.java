package top.teek.uac.system.model.po;

import top.teek.mp.annotation.FieldValueFill;
import top.teek.mp.annotation.ValueStrategy;
import top.teek.mp.base.BaseDO;
import top.teek.uac.system.model.vo.SysUserGroupVO;
import com.baomidou.mybatisplus.annotation.*;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Teeker
 * @date 2023-27-12 00:27:26
 * @note 用户组信息
*/
@TableName("t_sys_user_group")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysUserGroupVO.class, reverseConvertGenerate = false)
public class SysUserGroup extends BaseDO {
    /**
     * 用户组 ID
     */
    @TableField(fill = FieldFill.INSERT)
    @FieldValueFill(ValueStrategy.SNOWFLAKE)
    private String groupId;

    /**
     * 用户组名
     */
    private String groupName;

    /**
     * 用户组类型
     */
    private String groupType;

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
     * 租户编号
     */
    private String tenantId;

}