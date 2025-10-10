package top.teek.uac.system.model.po;

import top.teek.mp.base.BaseDO;
import top.teek.uac.system.model.vo.UserPostLinkVO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author Teeker
 * @date 2023-31-12 00:31:01
 * @note 用户关联岗位
*/
@TableName("t_user_post_link")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = UserPostLinkVO.class, reverseConvertGenerate = false)
@Accessors(chain = true)
public class UserPostLink extends BaseDO {
    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 岗位 ID
     */
    private String postId;

    /**
     * 租户编号
     */
    private String tenantId;

}