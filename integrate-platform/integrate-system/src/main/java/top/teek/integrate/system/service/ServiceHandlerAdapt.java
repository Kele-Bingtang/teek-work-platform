package top.teek.integrate.system.service;

import top.teek.integrate.system.model.bo.ConfigPoolBO;

/**
 * @author Teeker
 * @date 2024/10/28 00:30:35
 * @note
 */
public interface ServiceHandlerAdapt extends FlowService {
    boolean match(ConfigPoolBO config);
}