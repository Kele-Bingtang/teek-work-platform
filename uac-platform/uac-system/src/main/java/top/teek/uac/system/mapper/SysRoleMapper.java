package top.teek.uac.system.mapper;

import top.teek.uac.system.model.po.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-24-12 02:24:55
 * @note 针对表 t_sys_role(应用角色信息表)的数据库操作 Mapper
*/
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据用户 ID 查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRoleListByUserId(String userId);
}




