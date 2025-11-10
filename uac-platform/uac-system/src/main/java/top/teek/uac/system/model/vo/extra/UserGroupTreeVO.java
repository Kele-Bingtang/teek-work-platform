package top.teek.uac.system.model.vo.extra;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import top.teek.uac.system.model.po.SysUserGroup;

/**
 * @author Tianke
 * @date 2025/11/10 21:17:27
 * @since 1.0.0
 */
@Data
@AutoMapper(target = SysUserGroup.class, convertGenerate = false)
public class UserGroupTreeVO {
    /**
     * 用户组 ID
     */
    private String groupId;

    /**
     * 用户组名
     */
    private String groupName;
}
