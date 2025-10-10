package top.teek.uac.system.model.po;

import top.teek.mp.base.BaseDO;
import top.teek.uac.system.model.vo.RoleDeptLinkVO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author Teeker
 * @date 2023-44-11 23:44:15
 * @note 角色关联部门
*/
@TableName("t_role_dept_link")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = RoleDeptLinkVO.class, reverseConvertGenerate = false)
public class RoleDeptLink extends BaseDO {

    /**
     * 角色 ID
     */
    private String roleId;

    /**
     * 部门 ID
     */
    private String deptId;

    /**
     * 租户编号
     */
    private String tenantId;

    /**
     * 应用 ID
     */
    private String appId;

}