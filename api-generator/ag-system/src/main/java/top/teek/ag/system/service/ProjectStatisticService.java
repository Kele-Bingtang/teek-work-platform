package top.teek.ag.system.service;

import top.teek.ag.system.model.vo.ProjectStatisticVO;

/**
 * @author Teeker
 * @date 2024/8/4 15:39:25
 * @note
 */
public interface ProjectStatisticService {
    /**
     * 获取项目的统计数据
     *
     * @param projectId 项目 ID
     * @return 项目的统计数据
     */
    ProjectStatisticVO getBaseProjectStatistic(String projectId);
}
