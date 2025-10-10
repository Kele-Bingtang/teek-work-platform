package top.teek.uac.system.service;

import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.core.log.event.OperaLogEvent;
import top.teek.uac.system.model.dto.SysOperaLogDTO;
import top.teek.uac.system.model.po.SysOperaLog;
import top.teek.uac.system.model.vo.SysOperaLogVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_sys_opera_log(操作日志记录)】的数据库操作Service
 */
public interface SysOperaLogService extends IService<SysOperaLog> {

    /**
     * 记录操作日志
     *
     * @param operaLogEvent 操作日志事件
     */
    void recordOperaLog(OperaLogEvent operaLogEvent);

    /**
     * 通过条件查询操作日志清单列表
     *
     * @param sysOperaLogDTO 操作信息查询条件
     * @return 操作信息清单
     */
    List<SysOperaLogVO> listAll(SysOperaLogDTO sysOperaLogDTO);
        
    /**
     * 分页查询操作日志
     *
     * @param sysOperaLogDTO 操作日志DTO
     * @param pageQuery      分页查询
     * @return 操作日志VO
     */
    TablePage<SysOperaLogVO> listPage(SysOperaLogDTO sysOperaLogDTO, PageQuery pageQuery);

    /**
     * 批量删除操作日志
     *
     * @param ids 操作日志ID
     * @return 是否删除成功
     */
    Boolean removeBatch(List<Long> ids);

    /**
     * 清空所有操作日志
     *
     * @return 是否清空成功
     */
    Boolean cleanAllOperaLog();

}
