package top.teek.notice.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.teek.notice.system.model.po.NoticeInfo;
import top.teek.notice.system.service.NoticeInfoService;
import top.teek.notice.system.mapper.NoticeInfoMapper;
import org.springframework.stereotype.Service;

/**
 * @author Teeker
 * @date 2024-08-23 00:29:20
 * @note 针对表「t_notice_info（短信、邮件发送记录）」的数据库操作 Service 实现
 */
@Service
public class NoticeInfoServiceImpl extends ServiceImpl<NoticeInfoMapper, NoticeInfo>
        implements NoticeInfoService {

}




