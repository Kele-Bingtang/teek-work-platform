package top.teek.uac.system.mapper;

import top.teek.security.domain.SecurityUser;
import top.teek.uac.system.model.po.SysUser;
import top.teek.uac.system.model.vo.SysUserVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-25-12 02:25:27
 * @note 针对表 t_sys_user(用户信息表)的数据库操作 Mapper
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    SecurityUser selectTenantUserByUsername(@Param("tenantId") String tenantId, @Param("username") String username);

    SecurityUser selectUserByUsername(@Param("username") String username);

    List<SysUserVO> queryList(@Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);

    IPage<SysUserVO> selectListWithPage(@Param("page") Page<SysUser> page, @Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);

}




