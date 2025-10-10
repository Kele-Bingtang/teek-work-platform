package top.teek.ag.system.service.impl;

import top.teek.ag.system.mapper.CategoryMapper;
import top.teek.ag.system.mapper.ServiceInfoMapper;
import top.teek.ag.system.model.po.Category;
import top.teek.ag.system.model.po.ServiceInfo;
import top.teek.ag.system.model.vo.ProjectStatisticVO;
import top.teek.ag.system.service.ProjectStatisticService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Teeker
 * @date 2024/8/4 15:39:48
 * @note
 */
@Service
@RequiredArgsConstructor
public class ProjectStatisticServiceImpl implements ProjectStatisticService {
    
    private final ServiceInfoMapper serviceInfoMapper;
    private final CategoryMapper categoryMapper;
    
    @Override
    public ProjectStatisticVO getBaseProjectStatistic(String projectId) {
        Long serviceCount = serviceInfoMapper.selectCount(Wrappers.<ServiceInfo>lambdaQuery()
                .eq(ServiceInfo::getProjectId, projectId));

        Long categoryCount = categoryMapper.selectCount(Wrappers.<Category>lambdaQuery()
                .eq(Category::getProjectId, projectId));

        return ProjectStatisticVO.builder()
                .serviceCount(serviceCount)
                .categoryCount(categoryCount)
                .build();
    }
}
