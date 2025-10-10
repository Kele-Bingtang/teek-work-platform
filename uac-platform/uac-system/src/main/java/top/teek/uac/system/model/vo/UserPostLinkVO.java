package top.teek.uac.system.model.vo;

import lombok.Data;

/**
 * @author Teeker
 * @date 2023/12/28 0:08
 * @note
 */
@Data
public class UserPostLinkVO {
    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 岗位 ID
     */
    private String postId;

}
