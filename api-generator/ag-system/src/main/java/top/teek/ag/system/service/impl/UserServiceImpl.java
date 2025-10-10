package top.teek.ag.system.service.impl;

import top.teek.ag.core.helper.AgHelper;
import top.teek.ag.system.mapper.UserMapper;
import top.teek.ag.system.model.dto.UserDTO;
import top.teek.ag.system.model.po.User;
import top.teek.ag.system.model.vo.UserVO;
import top.teek.ag.system.service.UserService;
import top.teek.security.domain.LoginUser;
import top.teek.utils.MapstructUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2024-06-22 00:15:48
 * @note 针对表「t_user（用户表）」的数据库操作 Service 实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Value("${default.password}")
    private String password;

    @Override
    public List<UserVO> listByKeyword(String keyword) {
        List<User> userList = baseMapper.selectList(Wrappers.<User>lambdaQuery()
                .like(User::getUsername, keyword)
                .or()
                .like(User::getNickname, keyword)
        );

        return MapstructUtil.convert(userList, UserVO.class);
    }

    @Override
    public boolean updateByUsername(UserDTO userDTO) {
        User user = MapstructUtil.convert(userDTO, User.class);
        user.setPassword(null);
        return baseMapper.update(user, Wrappers.<User>lambdaUpdate()
                .eq(User::getUsername, userDTO.getUsername())) > 0;
    }

    @Override
    public boolean checkUsernameUnique(UserDTO userDTO) {
        return baseMapper.exists(Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, userDTO.getUsername())
                .ne(Objects.nonNull(userDTO.getId()), User::getId, userDTO.getId()));
    }

    @Override
    public boolean checkPhoneUnique(UserDTO userDTO) {
        return baseMapper.exists(Wrappers.<User>lambdaQuery()
                .eq(User::getPhone, userDTO.getPhone())
                .ne(Objects.nonNull(userDTO.getId()), User::getId, userDTO.getId()));
    }

    @Override
    public boolean checkEmailUnique(UserDTO userDTO) {
        return baseMapper.exists(Wrappers.<User>lambdaQuery()
                .eq(User::getEmail, userDTO.getEmail())
                .ne(Objects.nonNull(userDTO.getId()), User::getId, userDTO.getId()));
    }

    @Override
    public Boolean register(UserDTO userDTO) {
        User user = MapstructUtil.convert(userDTO, User.class);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setRegisterTime(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (Objects.isNull(user.getPassword())) {
            // 默认密码
            user.setPassword(passwordEncoder.encode(password));
        }
        final String SYSTEM = "System";
        user.setCreateBy(SYSTEM);
        user.setUpdateBy(SYSTEM);
        user.setCreateById(SYSTEM);
        user.setUpdateById(SYSTEM);
        return baseMapper.insert(user) > 0;
    }

    @Override
    public Boolean editUser(UserDTO userDTO) {
        User user = MapstructUtil.convert(userDTO, User.class);
        // userId、username、password 不允许编辑
        user.setUserId(null);
        user.setUsername(null);
        user.setPassword(null);

        int result = baseMapper.updateById(user);

        // 更新缓存的用户信息
        LoginUser loginUser = AgHelper.getLoginUser();

        if (Objects.nonNull(loginUser)) {
            loginUser.setAvatar(user.getAvatar());
            loginUser.setEmail(user.getEmail());
            loginUser.setNickname(user.getNickname());
            loginUser.setPhone(user.getPhone());
            loginUser.setSex(user.getSex());
            loginUser.setBirthday(user.getBirthday());

            AgHelper.updateUserInfo(loginUser);
        }
        return result > 0;
    }

    @Override
    public boolean updatePassword(String userId, String password) {
        return baseMapper.update(null,
                Wrappers.<User>lambdaUpdate()
                        .set(User::getPassword, password)
                        .eq(User::getUserId, userId)) > 0;
    }

}




