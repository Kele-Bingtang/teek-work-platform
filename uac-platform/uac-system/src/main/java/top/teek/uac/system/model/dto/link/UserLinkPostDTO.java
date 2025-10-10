package top.teek.uac.system.model.dto.link;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/4/20 18:02:40
 * @note
 */
@Data
@Accessors(chain = true)
public class UserLinkPostDTO {
    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 岗位 ID
     */
    private List<String> postIds;
    
}
