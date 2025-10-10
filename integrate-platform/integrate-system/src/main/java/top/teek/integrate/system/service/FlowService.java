package top.teek.integrate.system.service;

import top.teek.integrate.system.model.bo.ConfigPoolBO;

import java.util.Map;

/**
 * @author Teeker
 * @date 2024/10/28 00:29:53
 * @note
 */
public interface FlowService {
    
    Object auth(ConfigPoolBO config, Map<String, Object> authInfo);

    Object getData(ConfigPoolBO config, Map<String, Object> deliverInfo, Object authInfo);

    /**
     * 同步数据
     */
    Object syncDataRequest(ConfigPoolBO config, Map<String, Object> deliverInfo, Object data, Object authInfo);
}