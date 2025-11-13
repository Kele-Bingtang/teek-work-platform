package top.teek.uac.system.service.impl;

import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.security.domain.LoginUser;
import top.teek.uac.core.helper.UacHelper;
import top.teek.uac.core.log.event.OperaLogEvent;
import top.teek.uac.system.mapper.SysOperaLogMapper;
import top.teek.uac.system.model.dto.SysOperaLogDTO;
import top.teek.uac.system.model.po.SysDept;
import top.teek.uac.system.model.po.SysOperaLog;
import top.teek.uac.system.model.vo.SysOperaLogVO;
import top.teek.uac.system.service.SysDeptService;
import top.teek.uac.system.service.SysOperaLogService;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_sys_opera_log(操作日志记录)】的数据库操作Service实现
 */
@Service
@RequiredArgsConstructor
public class SysOperaLogServiceImpl extends ServiceImpl<SysOperaLogMapper, SysOperaLog> implements SysOperaLogService {

    private final SysDeptService sysDeptService;

    @Override
    public void recordOperaLog(OperaLogEvent operaLogEvent) {
        SysOperaLog sysOperaLog = MapstructUtil.convert(operaLogEvent, SysOperaLog.class);
        LoginUser loginUser = UacHelper.getLoginUser();
        // 添加用户部门
        if (Objects.nonNull(loginUser) && StringUtil.hasText(loginUser.getDeptId())) {
            SysDept sysDept = sysDeptService.getOne(Wrappers.<SysDept>lambdaQuery()
                    .select(SysDept::getDeptName)
                    .eq(SysDept::getDeptId, loginUser.getDeptId())
            );
            sysOperaLog.setDeptName(sysDept.getDeptName());
        }

        baseMapper.insert(sysOperaLog);
    }

    @Override
    public List<SysOperaLogVO> listAll(SysOperaLogDTO sysOperaLogDTO) {
        LambdaQueryWrapper<SysOperaLog> wrapper = buildQueryWrapper(sysOperaLogDTO);
        List<SysOperaLog> sysOperaLogList = baseMapper.selectList(wrapper);
        return MapstructUtil.convert(sysOperaLogList, SysOperaLogVO.class);
    }

    @Override
    public TablePage<SysOperaLogVO> listPage(SysOperaLogDTO sysOperaLogDTO, PageQuery pageQuery) {
        LambdaQueryWrapper<SysOperaLog> wrapper = buildQueryWrapper(sysOperaLogDTO);

        if (Objects.isNull(pageQuery.getOrderRuleList())) {
            wrapper.orderByDesc(SysOperaLog::getOperaId);
        }

        Page<SysOperaLog> sysOperaLogPage = baseMapper.selectPage(pageQuery.buildPage(), wrapper);

        return TablePage.build(sysOperaLogPage, SysOperaLogVO.class);
    }

    private LambdaQueryWrapper<SysOperaLog> buildQueryWrapper(SysOperaLogDTO sysOperaLogDTO) {
        LambdaQueryWrapper<SysOperaLog> wrapper = Wrappers.<SysOperaLog>lambdaQuery()
                .like(StringUtil.hasText(sysOperaLogDTO.getOperaName()), SysOperaLog::getOperaName, sysOperaLogDTO.getOperaName())
                .eq(Objects.nonNull(sysOperaLogDTO.getBusinessType()), SysOperaLog::getBusinessType, sysOperaLogDTO.getBusinessType())
                .like(StringUtil.hasText(sysOperaLogDTO.getTitle()), SysOperaLog::getTitle, sysOperaLogDTO.getTitle())
                .eq(Objects.nonNull(sysOperaLogDTO.getStatus()), SysOperaLog::getStatus, sysOperaLogDTO.getStatus())
                .like(StringUtil.hasText(sysOperaLogDTO.getOperaIp()), SysOperaLog::getOperaIp, sysOperaLogDTO.getOperaIp())
                .like(StringUtil.hasText(sysOperaLogDTO.getOperaLocation()), SysOperaLog::getOperaLocation, sysOperaLogDTO.getOperaLocation());
        if (Objects.nonNull(sysOperaLogDTO.getOperaTime())) {
            wrapper.between(SysOperaLog::getOperaTime, sysOperaLogDTO.getOperaTime().get(0), sysOperaLogDTO.getOperaTime().get(1));
        }

        return wrapper;
    }

    @Override
    public Boolean removeBatch(List<Long> ids) {
        return baseMapper.deleteByIds(ids) > 0;
    }

    @Override
    public Boolean cleanAllOperaLog() {
        return baseMapper.delete(new LambdaQueryWrapper<>()) > 0;
    }
}




