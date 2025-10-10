package top.teek.integrate.system.service;

import top.teek.integrate.system.model.dto.SyncFlowGetDTO;
import top.teek.integrate.system.model.dto.SyncFlowPostDTO;
import top.teek.integrate.system.model.dto.SyncFlowReceiveDataDTO;

/**
 * @author Teeker
 * @date 2024/10/28 00:22:25
 * @note
 */
public interface IntegrateService {
    
    Object getSourceAuth(SyncFlowGetDTO syncFlowGetDTO);

    Object requestSourceForGet(SyncFlowGetDTO syncBodyDTO);

    Object requestSourceForPost(SyncFlowPostDTO syncFlowPostDTO);

    Object toTargetFlowForReceiveData(SyncFlowReceiveDataDTO syncFlowReceiveDataDTO);

    Object sourceToTargetFlowForGet(SyncFlowGetDTO syncBodyDTO);

    Object sourceToTargetFlowForPost(SyncFlowPostDTO syncFlowPostDTO);
}