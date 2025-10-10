package top.teek.uac.system.model.vo.link;

import lombok.Data;

/**
 * @author Teeker
 * @date 2024/3/25 22:10
 * @note 用户穿梭框数据，如果 disabled 为 true，则禁选
 */
@Data
public class UserBindSelectVO {
    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 是否禁用，给前端穿梭框使用
     */
    private Boolean disabled;
    
}
