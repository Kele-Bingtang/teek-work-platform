package top.teek.file.system.service;

import top.teek.file.system.model.dto.FileOperaLogDTO;
import top.teek.file.system.model.po.FileOperaLog;
import top.teek.file.system.model.vo.FileOperaLogVO;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Teeker
 * @date 2024-08-06 21:08:58
 * @note 针对表「t_file_opera_log（附件信息日志表）」的数据库操作 Service
 */
public interface FileOperaLogService extends IService<FileOperaLog> {

    List<FileOperaLogVO> listAll(FileOperaLogDTO fileOperaLogDTO);
    
    TablePage<FileOperaLogVO> listPage(FileOperaLogDTO fileOperaLogDTO, PageQuery pageQuery);

    Boolean removeBatch(List<Long> ids);

    Boolean cleanAllOperaLog();

}
