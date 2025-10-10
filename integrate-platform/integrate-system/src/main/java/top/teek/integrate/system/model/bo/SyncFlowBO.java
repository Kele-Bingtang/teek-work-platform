package top.teek.integrate.system.model.bo;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * @author Teeker
 * @date 2024/10/28 00:49:17
 * @note
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncFlowBO {
    private ConfigPoolBO sourceInfo;
    private ConfigPoolBO targetInfo;
    private Object sourceData;
    private Map<String, Object> sourceAuthInfo;
    private Map<String, Object> targetAuthInfo;
    private Map<String, Object> sourceDeliverInfo;
    private Map<String, Object> targetDeliverInfo;
    private List<ConfigPoolBO> extraPoolBOList;
    private Boolean onlyGetAuth;
}