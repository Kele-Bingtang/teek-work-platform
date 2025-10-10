package top.teek.uac.system.model.dto.link;

import lombok.Data;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/4/24 23:48:28
 * @note
 */
@Data
public class RoleLinkDeptDTO {
    private String roleId;
    private String appId;
    private List<String> selectedDeptIds;
}
