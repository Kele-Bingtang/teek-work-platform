package top.teek.uac.system.mapper;

import top.teek.uac.system.model.po.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-24-12 02:24:13
 * @note 针对表 t_sys_menu(菜单表)的数据库操作 Mapper
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 通过用户 ID 获取有权限的菜单列表
     * @param appId 应用 ID
     * @param userId 用户 ID
     * @param onlyGetMenu 是否只获取菜单
     * @return 菜单列表
     */
    List<SysMenu> listMenuListByUserId(@Param("appId") String appId, @Param("userId") String userId, @Param("onlyGetMenu") boolean onlyGetMenu);

}




