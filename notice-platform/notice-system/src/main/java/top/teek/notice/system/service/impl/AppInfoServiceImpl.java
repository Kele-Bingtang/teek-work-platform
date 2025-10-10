package top.teek.notice.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.teek.notice.system.model.po.AppInfo;
import top.teek.notice.system.service.AppInfoService;
import top.teek.notice.system.mapper.AppInfoMapper;
import org.springframework.stereotype.Service;

/**
 * @author Teeker
 * @date 2024-08-23 00:29:20
 * @note 针对表「t_app_info（app 配置表）」的数据库操作 Service 实现
 */
@Service
public class AppInfoServiceImpl extends ServiceImpl<AppInfoMapper, AppInfo>
        implements AppInfoService {

}




