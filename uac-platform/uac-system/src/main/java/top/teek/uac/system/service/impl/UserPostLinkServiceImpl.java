package top.teek.uac.system.service.impl;

import top.teek.uac.system.mapper.UserPostLinkMapper;
import top.teek.uac.system.model.dto.link.UserLinkPostDTO;
import top.teek.uac.system.model.po.SysPost;
import top.teek.uac.system.model.po.UserPostLink;
import top.teek.uac.system.service.UserPostLinkService;
import top.teek.utils.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_user_post_link(用户关联岗位表)】的数据库操作Service实现
 */
@Service
public class UserPostLinkServiceImpl extends ServiceImpl<UserPostLinkMapper, UserPostLink> implements UserPostLinkService {

    @Override
    public List<SysPost> listPostByUserId(String userId) {

        QueryWrapper<UserPostLink> wrapper = Wrappers.query();
        wrapper.eq("tupl.is_deleted", 0)
                .eq("tupl.user_id", userId);
        
        return baseMapper.listPostByUserId(wrapper);
    }

    @Override
    public boolean checkPostExistUser(List<String> postIds) {
        return baseMapper.exists(Wrappers.<UserPostLink>lambdaQuery()
                .in(UserPostLink::getPostId, postIds));
    }

    @Override
    public boolean addPostsToUser(UserLinkPostDTO userLinkPostDTO) {
        List<String> postIds = userLinkPostDTO.getPostIds();

        List<UserPostLink> userRoleLinkList = ListUtil.newArrayList(postIds, postId ->
                        new UserPostLink().setPostId(postId)
                                .setUserId(userLinkPostDTO.getUserId())
                , UserPostLink.class);

        return Db.saveBatch(userRoleLinkList);
    }

}




