package top.teek.uac.system.service;

import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.system.model.dto.SysUserGroupDTO;
import top.teek.uac.system.model.po.SysUserGroup;
import top.teek.uac.system.model.vo.SysUserGroupVO;
import com.baomidou.mybatisplus.extension.service.IService;
import top.teek.uac.system.model.vo.extra.UserGroupTreeVO;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_sys_user_group(用户组表)】的数据库操作Service
 */
public interface SysUserGroupService extends IService<SysUserGroup> {

    /**
     * 通过条件查询用户组信息
     *
     * @return 用户组信息
     */
    List<SysUserGroupVO> listAll(SysUserGroupDTO sysUserGroupDTO);
    
    /**
     * 通过条件查询用户组信息
     *
     * @param sysUserGroupDTO 查询条件
     * @param pageQuery       分页条件
     * @return 用户组信息
     */
    TablePage<SysUserGroupVO> listPage(SysUserGroupDTO sysUserGroupDTO, PageQuery pageQuery);

    /**
     * 获取用户组树列表
     *
     * @return 用户组树列表
     */
    List<UserGroupTreeVO> listTreeList();

    /**
     * 检查用户组名是否唯一
     *
     * @param sysUserGroupDTO 用户组信息
     * @return 是否唯一
     */
    boolean checkUserGroupNameUnique(SysUserGroupDTO sysUserGroupDTO);

    /**
     * 新增用户组
     *
     * @param sysUserGroupDTO 用户组信息
     * @return 是否新增成功
     */
    boolean addUserGroup(SysUserGroupDTO sysUserGroupDTO);

    /**
     * 修改用户组
     *
     * @param sysUserGroupDTO 用户组信息
     * @return 是否修改成功
     */
    boolean editOne(SysUserGroupDTO sysUserGroupDTO);

    /**
     * 批量删除用户组
     *
     * @param ids 主键列表
     * @return 是否删除成功
     */
    boolean removeBatch(List<Long> ids, List<String> userGroupIds);

}
