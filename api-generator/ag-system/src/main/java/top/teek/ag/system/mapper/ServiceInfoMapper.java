package top.teek.ag.system.mapper;

import top.teek.ag.system.model.po.ServiceInfo;
import top.teek.ag.system.model.vo.ServiceInfoVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author Teeker
 * @date 2024-23-22 00:23:11
 * @note 针对表「t_service（服务表）」的数据库操作 Mapper
 */
public interface ServiceInfoMapper extends BaseMapper<ServiceInfo> {

    Page<ServiceInfoVO> selectPageData(Page<ServiceInfo> objectPage, @Param(Constants.WRAPPER) Wrapper<ServiceInfo> queryWrapper);
}




