package top.teek.uac.system.service.impl;

import top.teek.core.constants.ColumnConstant;
import top.teek.uac.core.event.LoginInfoEvent;
import top.teek.core.exception.ServiceException;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.security.domain.LoginUser;
import top.teek.uac.core.constant.AuthConstant;
import top.teek.uac.core.helper.UacHelper;
import top.teek.uac.system.mapper.SysUserMapper;
import top.teek.uac.system.model.dto.SysUserDTO;
import top.teek.uac.system.model.dto.link.UserLinkPostDTO;
import top.teek.uac.system.model.po.*;
import top.teek.uac.system.model.vo.SysUserVO;
import top.teek.uac.system.service.*;
import top.teek.uac.system.model.po.*;
import top.teek.utils.MapstructUtil;
import top.teek.web.utils.ServletUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.teek.uac.system.service.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_sys_user(用户信息表)】的数据库操作Service实现
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Value("${default.password}")
    private String password;

    private final SysDeptService sysDeptService;
    private final UserRoleLinkService userRoleLinkService;
    private final UserPostLinkService userPostLinkService;
    private final UserGroupLinkService userGroupLinkService;

    @Override
    public List<SysUserVO> listAll(SysUserDTO sysUserDTO) {
        QueryWrapper<SysUser> wrapper = buildQueryWrapper(sysUserDTO);
        return baseMapper.queryList(wrapper);
    }

    @Override
    public TablePage<SysUserVO> listPage(SysUserDTO sysUserDTO, PageQuery pageQuery) {
        QueryWrapper<SysUser> wrapper = buildQueryWrapper(sysUserDTO);
        IPage<SysUserVO> sysUserVOIPage = baseMapper.selectListWithPage(pageQuery.buildPage(), wrapper);

        sysUserVOIPage.getRecords().forEach(sysUserVO -> {
            if (StringUtil.hasText(sysUserVO.getPostId())) {
                sysUserVO.setPostIds(List.of(sysUserVO.getPostId().split(",")));
            }
        });

        return TablePage.build(sysUserVOIPage);
    }

    private QueryWrapper<SysUser> buildQueryWrapper(SysUserDTO sysUserDTO) {
        QueryWrapper<SysUser> wrapper = Wrappers.query();
        wrapper.eq("su.is_deleted", ColumnConstant.NON_DELETED)
                .eq(StringUtil.hasText(sysUserDTO.getUserId()), "su.user_id", sysUserDTO.getUserId())
                .like(StringUtil.hasText(sysUserDTO.getUsername()), "su.username", sysUserDTO.getUsername())
                .like(StringUtil.hasText(sysUserDTO.getPhone()), "su.phone", sysUserDTO.getPhone())
                .like(StringUtil.hasText(sysUserDTO.getEmail()), "su.email", sysUserDTO.getEmail())
                .eq(Objects.nonNull(sysUserDTO.getStatus()), "su.status", sysUserDTO.getStatus())
                .and(StringUtil.hasText(sysUserDTO.getDeptId()), qw -> {
                    // 查出 deptId 所对应的部门及其子部门 ID 信息
                    List<SysDept> sysDeptList = sysDeptService.list(Wrappers.<SysDept>lambdaQuery()
                            .select(SysDept::getDeptId)
                            .apply("FIND_IN_SET({0}, ancestors) > 0", sysUserDTO.getDeptId()));
                    // 获取所有子部门 ID
                    List<String> deptIds = sysDeptList.stream().map(SysDept::getDeptId).filter(Objects::nonNull).collect(Collectors.toList());
                    // 加上当前部门
                    deptIds.add(sysUserDTO.getDeptId());
                    qw.in("su.dept_id", deptIds);
                })
                .orderByAsc("su.id");

        return wrapper;
    }

    @Override
    public boolean checkUserNameUnique(SysUserDTO sysUserDTO) {
        return baseMapper.exists(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getUsername, sysUserDTO.getUsername())
                .ne(Objects.nonNull(sysUserDTO.getId()), SysUser::getId, sysUserDTO.getId()));
    }

    @Override
    public boolean checkPhoneUnique(SysUserDTO sysUserDTO) {
        return baseMapper.exists(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getPhone, sysUserDTO.getPhone())
                .ne(Objects.nonNull(sysUserDTO.getId()), SysUser::getId, sysUserDTO.getId()));
    }

    @Override
    public boolean checkEmailUnique(SysUserDTO sysUserDTO) {
        return baseMapper.exists(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getEmail, sysUserDTO.getEmail())
                .ne(Objects.nonNull(sysUserDTO.getId()), SysUser::getId, sysUserDTO.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(SysUserDTO sysUserDTO) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        SysUser sysUser = MapstructUtil.convert(sysUserDTO, SysUser.class);
        sysUser.setRegisterTime(LocalDateTime.now());
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        if (Objects.isNull(sysUser.getPassword())) {
            sysUser.setPassword(passwordEncoder.encode(password));
        }
        int insert = baseMapper.insert(sysUser);

        if (Objects.nonNull(sysUserDTO.getPostId())) {
            addOrUpdatePost(sysUserDTO, sysUser.getUserId(), false);
        }

        return insert > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editUser(SysUserDTO sysUserDTO) {
        checkUserAllowed(sysUserDTO.getUserId());
        SysUser sysUser = MapstructUtil.convert(sysUserDTO, SysUser.class);
        // userId、username、password 不允许编辑
        sysUser.setUserId(null);
        sysUser.setUsername(null);
        sysUser.setPassword(null);
        int insert = baseMapper.updateById(sysUser);

        if (Objects.nonNull(sysUserDTO.getPostId())) {
            addOrUpdatePost(sysUserDTO, sysUserDTO.getUserId(), true);
        }

        // 更新缓存的用户信息
        LoginUser loginUser = UacHelper.getLoginUser();
        if (Objects.nonNull(loginUser)) {
            loginUser.setAvatar(sysUser.getAvatar());
            loginUser.setDeptId(sysUser.getDeptId());
            loginUser.setEmail(sysUser.getEmail());
            loginUser.setNickname(sysUser.getNickname());
            loginUser.setPhone(sysUser.getPhone());
            loginUser.setSex(sysUser.getSex());
            loginUser.setBirthday(sysUser.getBirthday());

            UacHelper.updateUserInfo(loginUser);
        }

        return insert > 0;
    }

    @Override
    public boolean updatePassword(String userId, String password) {
        checkUserAllowed(userId);
        return baseMapper.update(null,
                Wrappers.<SysUser>lambdaUpdate()
                        .set(SysUser::getPassword, password)
                        .eq(SysUser::getUserId, userId)) > 0;
    }

    /**
     * 添加或更新用户与岗位、角色关联
     *
     * @param sysUserDTO 用户信息
     * @param userId     用户 ID
     */
    private void addOrUpdatePost(SysUserDTO sysUserDTO, String userId, boolean removeLink) {
        if (removeLink) {
            // 删除用户与岗位表
            userPostLinkService.remove(Wrappers.<UserPostLink>lambdaQuery().eq(UserPostLink::getUserId, sysUserDTO.getUserId()));
        }

        List<String> postId = sysUserDTO.getPostId();
        if (Objects.nonNull(postId)) {
            UserLinkPostDTO userLinkPostDTO = new UserLinkPostDTO().setUserId(userId)
                    .setPostIds(postId);
            userPostLinkService.addPostsToUser(userLinkPostDTO);
        }
    }

    @Override
    public boolean updateByUserId(SysUserDTO sysUserDTO) {
        checkUserAllowed(sysUserDTO.getUserId());
        SysUser sysUser = MapstructUtil.convert(sysUserDTO, SysUser.class);
        sysUser.setPassword(null);
        return baseMapper.update(sysUser, Wrappers.<SysUser>lambdaUpdate()
                .eq(SysUser::getUsername, sysUserDTO.getUsername())) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeBatch(List<Long> ids, List<String> userIds) {
        // 校验删除的是否为超级管理员用户
        userIds.forEach(this::checkUserAllowed);
        // 删除用户与角色关联
        userRoleLinkService.remove(Wrappers.<UserRoleLink>lambdaQuery().in(UserRoleLink::getUserId, userIds));
        // 删除用户与岗位表
        userPostLinkService.remove(Wrappers.<UserPostLink>lambdaQuery().in(UserPostLink::getUserId, userIds));
        // 删除用户与用户组表
        userGroupLinkService.remove(Wrappers.<UserGroupLink>lambdaQuery().in(UserGroupLink::getUserId, userIds));

        return baseMapper.deleteByIds(ids) > 0;
    }

    /**
     * 登录成功的信息记录
     */
    @Async
    @EventListener
    public void updateUserStatus(LoginInfoEvent loginInfoEvent) {
        if (!loginInfoEvent.getStatus().equals(AuthConstant.LOGIN_SUCCESS)) {
            return;
        }
        // 登录成功则往下走
        String userId = loginInfoEvent.getUserId();
        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setLoginIp(ServletUtil.getClientIp(loginInfoEvent.getRequest()));
        user.setLoginTime(LocalDateTime.now());
        user.setUpdateBy(userId);

        baseMapper.update(user, Wrappers.<SysUser>lambdaUpdate().eq(SysUser::getUserId, userId));
    }

    @Override
    public void checkUserAllowed(String userId) {
        if (Objects.nonNull(userId) && UacHelper.isAdmin(userId)) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }
}




