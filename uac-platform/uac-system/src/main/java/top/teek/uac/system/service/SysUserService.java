package top.teek.uac.system.service;

import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.system.model.dto.SysUserDTO;
import top.teek.uac.system.model.po.SysUser;
import top.teek.uac.system.model.vo.SysUserVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_sys_user(用户信息表)】的数据库操作Service
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 通过条件查询用户列表
     *
     * @param sysUserDTO 用户信息
     * @return 用户列表
     */
    List<SysUserVO> listAll(SysUserDTO sysUserDTO);

    /**
     * 通过条件查询用户列表
     *
     * @param sysUserDTO 用户信息
     * @param pageQuery  分页参数
     * @return 用户列表
     */
    TablePage<SysUserVO> listPage(SysUserDTO sysUserDTO, PageQuery pageQuery);

    /**
     * 检查用户名是否唯一通过条件查询用户列表
     *
     * @param sysUserDTO 用户信息
     * @return 是否唯一
     */
    boolean checkUserNameUnique(SysUserDTO sysUserDTO);

    /**
     * 检查手机号是否唯一
     *
     * @param sysUserDTO 用户信息
     * @return 是否唯一
     */
    boolean checkPhoneUnique(SysUserDTO sysUserDTO);

    /**
     * 检查邮箱是否唯一
     *
     * @param sysUserDTO 用户信息
     * @return 是否唯一
     */
    boolean checkEmailUnique(SysUserDTO sysUserDTO);

    /**
     * 新增用户
     *
     * @param sysUserDTO 用户信息
     * @return 是否成功
     */
    boolean addUser(SysUserDTO sysUserDTO);

    /**
     * 更新用户
     *
     * @param sysUserDTO 用户信息
     * @return 是否成功
     */
    boolean editUser(SysUserDTO sysUserDTO);

    /**
     * 更新用户密码
     *
     * @param userId 用户 ID
     * @param password 密码
     * @return 是否成功
     */
    boolean updatePassword(String userId, String password);

    /**
     * 通过用户 ID 更新用户
     *
     * @param sysUserDTO 用户信息
     * @return 是否成功
     */
    boolean updateByUserId(SysUserDTO sysUserDTO);

    /**
     * 批量删除用户
     *
     * @param ids 主键列表
     * @return 是否成功
     */
    boolean removeBatch(List<Long> ids, List<String> userIds);

    /**
     * 校验用户是否允许操作
     *
     * @param userId 用户 ID
     */
    void checkUserAllowed(String userId);
}
