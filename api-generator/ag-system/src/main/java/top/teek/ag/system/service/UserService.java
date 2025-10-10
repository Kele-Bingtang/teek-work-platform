package top.teek.ag.system.service;

import top.teek.ag.system.model.dto.UserDTO;
import top.teek.ag.system.model.po.User;
import top.teek.ag.system.model.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Teeker
 * @date 2024-06-22 00:15:48
 * @note 针对表「t_user（用户表）」的数据库操作 Service
 */
public interface UserService extends IService<User> {

    /**
     * 根据关键字查询用户
     *
     * @param keyword 关键词
     * @return 用户列表
     */
    List<UserVO> listByKeyword(String keyword);

    /**
     * 根据用户 ID 更新用户信息
     *
     * @param userDTO 用户信息
     * @return 成功与否
     */
    boolean updateByUsername(UserDTO userDTO);

    /**
     * 检查用户名是否唯一
     *
     * @param userDTO 用户信息
     * @return 是否唯一
     */
    boolean checkUsernameUnique(UserDTO userDTO);

    /**
     * 检查手机号是否唯一
     *
     * @param userDTO 用户信息
     * @return 是否唯一
     */
    boolean checkPhoneUnique(UserDTO userDTO);

    /**
     * 检查邮箱是否唯一
     *
     * @param userDTO 用户信息
     * @return 是否唯一
     */
    boolean checkEmailUnique(UserDTO userDTO);

    /**
     * 用户注册
     *
     * @param userDTO 用户信息
     * @return 是否成功
     */
    Boolean register(UserDTO userDTO);

    /**
     * 用户编辑
     *
     * @param userDTO 用户信息
     * @return 是否成功
     */
    Boolean editUser(UserDTO userDTO);

    /**
     * 更新用户密码
     *
     * @param userId 用户 ID
     * @param password 密码
     * @return 是否成功
     */
    boolean updatePassword(String userId, String password);
}
