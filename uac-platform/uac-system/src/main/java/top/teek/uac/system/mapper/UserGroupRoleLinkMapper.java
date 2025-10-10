package top.teek.uac.system.mapper;

import top.teek.uac.system.model.po.UserGroupLink;
import top.teek.uac.system.model.po.UserGroupRoleLink;
import top.teek.uac.system.model.vo.link.RoleBindSelectVO;
import top.teek.uac.system.model.vo.link.RoleLinkVO;
import top.teek.uac.system.model.vo.link.UserGroupBindSelectVO;
import top.teek.uac.system.model.vo.link.UserGroupLinkVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-26-12 02:26:00
 * @note 针对表 t_user_group_role_link(用户组关联角色表)的数据库操作 Mapper
 */
public interface UserGroupRoleLinkMapper extends BaseMapper<UserGroupRoleLink> {

    IPage<RoleLinkVO> listRoleLinkByGroupId(@Param("page") Page<UserGroupLink> page, @Param(Constants.WRAPPER) Wrapper<UserGroupLink> queryWrapper);

    IPage<UserGroupLinkVO> listUserGroupByRoleId(@Param("page") Page<UserGroupLink> page, @Param(Constants.WRAPPER) Wrapper<UserGroupLink> queryWrapper);

    List<RoleBindSelectVO> listWithDisabledByGroupId(@Param("userGroupId") String userGroupId);

    List<UserGroupBindSelectVO> listWithDisabledByRoleId(@Param("roleId") String roleId);
    
}




