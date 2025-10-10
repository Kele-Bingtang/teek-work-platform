package top.teek.uac.system.service;

import top.teek.uac.system.model.dto.link.UserLinkPostDTO;
import top.teek.uac.system.model.po.SysPost;
import top.teek.uac.system.model.po.UserPostLink;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_user_post_link(用户关联岗位表)】的数据库操作Service
 */
public interface UserPostLinkService extends IService<UserPostLink> {

    /**
     * 根据用户 ID 查询岗位列表
     *
     * @param userId 用户 ID
     * @return 岗位列表
     */
    List<SysPost> listPostByUserId(String userId);

    /**
     * 检查岗位是否绑定过用户
     *
     * @param postIds 多个岗位 ID
     * @return 是否存在绑定
     */
    boolean checkPostExistUser(List<String> postIds);

    /**
     * 用户绑定多个岗位
     *
     * @param userLinkPostDTO 用户关联岗位信息
     * @return 是否绑定成功
     */
    boolean addPostsToUser(UserLinkPostDTO userLinkPostDTO);
}
