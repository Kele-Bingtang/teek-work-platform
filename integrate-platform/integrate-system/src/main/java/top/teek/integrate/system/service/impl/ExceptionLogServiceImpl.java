package top.teek.integrate.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.teek.integrate.system.model.po.ExceptionLog;
import top.teek.integrate.system.service.ExceptionLogService;
import top.teek.integrate.system.mapper.ExceptionLogMapper;
import org.springframework.stereotype.Service;

/**
 * @author Teeker
 * @date 2024-10-28 00:45:43
 * @note 针对表【sis_tbl_exception_log】的数据库操作 Service 实现
 */
@Service
public class ExceptionLogServiceImpl extends ServiceImpl<ExceptionLogMapper, ExceptionLog> implements ExceptionLogService {

}




