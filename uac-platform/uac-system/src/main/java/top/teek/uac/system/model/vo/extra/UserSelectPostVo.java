package top.teek.uac.system.model.vo.extra;

import top.teek.uac.system.model.vo.SysPostVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/2/5 23:25
 * @note
 */
@Data
@Accessors(chain = true)
public class UserSelectPostVo {
    private List<SysPostVO> postList;
    private List<String> postIds;
}
