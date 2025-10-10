package top.teek.uac.system.mapper;

import top.teek.uac.system.model.po.SysPost;
import top.teek.uac.system.model.po.UserPostLink;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-26-12 02:26:10
 * @note 针对表 t_user_post_link(用户关联岗位表)的数据库操作 Mapper
*/
public interface UserPostLinkMapper extends BaseMapper<UserPostLink> {

    List<SysPost> listPostByUserId(@Param(Constants.WRAPPER) Wrapper<UserPostLink> queryWrapper);
}




