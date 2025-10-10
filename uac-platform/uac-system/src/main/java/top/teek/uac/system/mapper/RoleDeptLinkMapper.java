package top.teek.uac.system.mapper;

import top.teek.uac.system.model.po.RoleDeptLink;
import top.teek.uac.system.model.po.SysDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-22-12 02:22:04
 * @note 针对表 t_role_dept_link(角色关联部门表)的数据库操作 Mapper
 */
public interface RoleDeptLinkMapper extends BaseMapper<RoleDeptLink> {

    List<SysDept> listDeptListByRoleId(@Param("roleId") String roleId, @Param("appId") String appId);
}




