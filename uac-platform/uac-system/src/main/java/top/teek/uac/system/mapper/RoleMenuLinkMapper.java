package top.teek.uac.system.mapper;

import top.teek.uac.system.model.po.RoleMenuLink;
import top.teek.uac.system.model.po.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-22-12 02:22:59
 * @note 针对表 t_role_menu_link(角色关联菜单表)的数据库操作 Mapper
 */
public interface RoleMenuLinkMapper extends BaseMapper<RoleMenuLink> {

    List<SysMenu> listMenuListByRoleId(@Param("roleId") String roleId, @Param("appId") String appId);
}




