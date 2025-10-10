package top.teek.uac.system.service;

import top.teek.uac.system.model.dto.link.RoleLinkDeptDTO;
import top.teek.uac.system.model.po.RoleDeptLink;
import top.teek.uac.system.model.po.SysDept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_role_dept_link(角色关联部门表)】的数据库操作Service
 */
public interface RoleDeptLinkService extends IService<RoleDeptLink> {

    /**
     * 根据角色 ID 获取部门列表
     * @param roleId 角色 ID
     * @param appId 应用 ID
     * @return 部门列表
     */
    List<SysDept> listDeptListByRoleId(String roleId, String appId);

    /**
     * 添加部门到角色
     *
     * @param roleLinkDeptDTO 部门关联角色信息
     * @param removeLink 是否移除角色关联的部门
     * @return 是否添加成功部门关联角色信息
     */
    Boolean addDeptsToRole(RoleLinkDeptDTO roleLinkDeptDTO, boolean removeLink);
}
