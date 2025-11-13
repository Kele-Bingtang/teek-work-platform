package top.teek.uac.system.service.impl;

import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.system.mapper.SysPostMapper;
import top.teek.uac.system.model.dto.SysPostDTO;
import top.teek.uac.system.model.po.SysDept;
import top.teek.uac.system.model.po.SysPost;
import top.teek.uac.system.model.po.UserPostLink;
import top.teek.uac.system.model.vo.SysPostVO;
import top.teek.uac.system.model.vo.extra.UserSelectPostVo;
import top.teek.uac.system.service.SysDeptService;
import top.teek.uac.system.service.SysPostService;
import top.teek.uac.system.service.UserPostLinkService;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_sys_post(岗位表)】的数据库操作Service实现
 */
@Service
@RequiredArgsConstructor
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements SysPostService {

    private final UserPostLinkService userPostLinkService;
    private final SysDeptService sysDeptService;

    @Override
    public List<SysPostVO> listAll(SysPostDTO sysPostDTO) {
        LambdaQueryWrapper<SysPost> wrapper = buildQueryWrapper(sysPostDTO);
        List<SysPost> sysPostList = baseMapper.selectList(wrapper);

        return MapstructUtil.convert(sysPostList, SysPostVO.class);
    }

    @Override
    public TablePage<SysPostVO> listPage(SysPostDTO sysPostDTO, PageQuery pageQuery) {
        LambdaQueryWrapper<SysPost> wrapper = buildQueryWrapper(sysPostDTO);
        Page<SysPost> sysPostPage = baseMapper.selectPage(pageQuery.buildPage(), wrapper);

        return TablePage.build(sysPostPage, SysPostVO.class);
    }

    private LambdaQueryWrapper<SysPost> buildQueryWrapper(SysPostDTO sysPostDTO) {
        return Wrappers.<SysPost>lambdaQuery()
                .eq(StringUtil.hasText(sysPostDTO.getPostCode()), SysPost::getPostCode, sysPostDTO.getPostCode())
                .eq(StringUtil.hasText(sysPostDTO.getPostName()), SysPost::getPostName, sysPostDTO.getPostName())
                .eq(Objects.nonNull(sysPostDTO.getStatus()), SysPost::getStatus, sysPostDTO.getStatus())
                .and(StringUtil.hasText(sysPostDTO.getDeptId()), qw -> {
                    // 查出 deptId 所对应的部门及其子部门 ID 信息
                    List<SysDept> sysDeptList = sysDeptService.list(Wrappers.<SysDept>lambdaQuery()
                            .select(SysDept::getDeptId)
                            .apply("FIND_IN_SET({0}, ancestors) > 0", sysPostDTO.getDeptId()));
                    // 获取所有子部门 ID
                    List<String> deptIds = sysDeptList.stream().map(SysDept::getDeptId).filter(Objects::nonNull).collect(Collectors.toList());
                    // 加上当前部门
                    deptIds.add(sysPostDTO.getDeptId());
                    qw.in(SysPost::getDeptId, deptIds);
                })
                .orderByAsc(SysPost::getOrderNum);
    }

    @Override
    public boolean checkPostNameUnique(SysPostDTO sysPostDTO) {
        return baseMapper.exists(new LambdaQueryWrapper<SysPost>()
                .eq(SysPost::getPostName, sysPostDTO.getPostName())
                .ne(Objects.nonNull(sysPostDTO.getId()), SysPost::getId, sysPostDTO.getId()));
    }

    @Override
    public boolean checkPostCodeUnique(SysPostDTO sysPostDTO) {
        return baseMapper.exists(new LambdaQueryWrapper<SysPost>()
                .eq(SysPost::getPostCode, sysPostDTO.getPostCode())
                .ne(Objects.nonNull(sysPostDTO.getId()), SysPost::getId, sysPostDTO.getId()));
    }

    @Override
    public boolean checkPostExitUser(SysPostDTO sysPostDTO) {
        return userPostLinkService.exists(new LambdaQueryWrapper<UserPostLink>()
                .eq(UserPostLink::getPostId, sysPostDTO.getPostId()));
    }

    @Override
    public boolean addPost(SysPostDTO sysPostDTO) {
        SysPost sysPost = MapstructUtil.convert(sysPostDTO, SysPost.class);
        return baseMapper.insert(sysPost) > 0;
    }

    @Override
    public boolean editPost(SysPostDTO sysPostDTO) {
        SysPost sysPost = MapstructUtil.convert(sysPostDTO, SysPost.class);
        return baseMapper.updateById(sysPost) > 0;
    }

    @Override
    public boolean removeBatch(List<Long> ids) {
        return baseMapper.deleteByIds(ids) > 0;
    }

    @Override
    public UserSelectPostVo userSelectPostList(String userId) {
        List<SysPostVO> sysPostVOList = listAll(new SysPostDTO());
        List<SysPost> sysPosts = userPostLinkService.listPostByUserId(userId);

        UserSelectPostVo userSelectPostVo = new UserSelectPostVo()
                .setPostList(sysPostVOList)
                .setPostIds(sysPosts.stream().map(SysPost::getPostId).toList());

        return userSelectPostVo;
    }
}




