package top.teek.integrate.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.teek.integrate.system.model.po.AppInfo;
import top.teek.integrate.system.service.AppInfoService;
import top.teek.integrate.system.mapper.AppInfoMapper;
import org.springframework.stereotype.Service;

/**
 * @author Teeker
 * @date 2024-10-28 00:45:43
 * @note 针对表【sis_tbl_app_info(系统信息)】的数据库操作 Service 实现
 */
@Service
public class AppInfoServiceImpl extends ServiceImpl<AppInfoMapper, AppInfo> implements AppInfoService {

}




