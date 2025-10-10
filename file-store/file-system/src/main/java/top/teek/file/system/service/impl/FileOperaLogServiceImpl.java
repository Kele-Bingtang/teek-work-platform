package top.teek.file.system.service.impl;

import top.teek.file.system.model.dto.FileOperaLogDTO;
import top.teek.file.system.model.vo.FileOperaLogVO;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.teek.file.system.model.po.FileOperaLog;
import top.teek.file.system.service.FileOperaLogService;
import top.teek.file.system.mapper.FileOperaLogMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @note 针对表「t_file_opera_log（附件信息日志表）」的数据库操作 Service 实现
 * @date 2024-08-06 21:08:58
 */
@Service
public class FileOperaLogServiceImpl extends ServiceImpl<FileOperaLogMapper, FileOperaLog> implements FileOperaLogService {

    public List<FileOperaLogVO> listAll(FileOperaLogDTO fileOperaLogDTO) {
        LambdaQueryWrapper<FileOperaLog> wrapper = buildQueryWrapper(fileOperaLogDTO);
        List<FileOperaLog> sysOperaLogList = baseMapper.selectList(wrapper);
        return MapstructUtil.convert(sysOperaLogList, FileOperaLogVO.class);
    }
    
    public TablePage<FileOperaLogVO> listPage(FileOperaLogDTO fileOperaLogDTO, PageQuery pageQuery) {
        LambdaQueryWrapper<FileOperaLog> wrapper = buildQueryWrapper(fileOperaLogDTO);

        if (Objects.isNull(pageQuery.getOrderRuleList())) {
            wrapper.orderByDesc(FileOperaLog::getOperaId);
        }

        Page<FileOperaLog> sysOperaLogPage = baseMapper.selectPage(pageQuery.buildPage(), wrapper);

        return TablePage.build(sysOperaLogPage, FileOperaLogVO.class);
    }

    private LambdaQueryWrapper<FileOperaLog> buildQueryWrapper(FileOperaLogDTO fileOperaLogDTO) {
        LambdaQueryWrapper<FileOperaLog> wrapper = Wrappers.<FileOperaLog>lambdaQuery()
                .like(StringUtil.hasText(fileOperaLogDTO.getAppId()), FileOperaLog::getAppId, fileOperaLogDTO.getAppId())
                .like(StringUtil.hasText(fileOperaLogDTO.getFileKey()), FileOperaLog::getFileKey, fileOperaLogDTO.getFileKey())
                .like(StringUtil.hasText(fileOperaLogDTO.getOperaUser()), FileOperaLog::getOperaUser, fileOperaLogDTO.getOperaUser())
                .eq(Objects.nonNull(fileOperaLogDTO.getOperaType()), FileOperaLog::getOperaType, fileOperaLogDTO.getOperaType())
                .like(StringUtil.hasText(fileOperaLogDTO.getOperaIp()), FileOperaLog::getOperaIp, fileOperaLogDTO.getOperaIp())
                .like(StringUtil.hasText(fileOperaLogDTO.getOperaLocation()), FileOperaLog::getOperaLocation, fileOperaLogDTO.getOperaLocation());
        if (Objects.nonNull(fileOperaLogDTO.getCreateTime())) {
            wrapper.between(FileOperaLog::getCreateTime, fileOperaLogDTO.getCreateTime().get(0), fileOperaLogDTO.getCreateTime().get(1));
        }

        return wrapper;
    }

    @Override
    public Boolean removeBatch(List<Long> ids) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Boolean cleanAllOperaLog() {
        return baseMapper.delete(new LambdaQueryWrapper<>()) > 0;
    }

}




