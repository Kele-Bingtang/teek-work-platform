package top.teek.uac.system.service.impl;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import top.teek.uac.core.event.LoginInfoEvent;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.core.constant.AuthConstant;
import top.teek.uac.system.mapper.SysLoginLogMapper;
import top.teek.uac.system.model.dto.SysLoginLogDTO;
import top.teek.uac.system.model.dto.SysUserDTO;
import top.teek.uac.system.model.po.SysLoginLog;
import top.teek.uac.system.model.vo.SysLoginLogVO;
import top.teek.uac.system.service.SysLoginLogService;
import top.teek.uac.system.service.SysUserService;
import top.teek.utils.AddressUtil;
import top.teek.utils.MapstructUtil;
import top.teek.web.utils.ServletUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_sys_login_info(操作日志记录)】的数据库操作Service实现
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {

    private final SysUserService sysUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordLoginLog(LoginInfoEvent loginInfoEvent) {
        HttpServletRequest request = loginInfoEvent.getRequest();
        // 获取 IP
        String clientIp = ServletUtil.getClientIp(request);
        String address = AddressUtil.getRealAddressByIp(clientIp);
        // 获取客户端标识
        UserAgent userAgent = UserAgentUtil.parse(ServletUtil.getUserAgent(request));
        // 获取客户端操作系统
        String os = userAgent.getOs().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();

        log.info("用户 {} 在 {} - {} 下 {}", loginInfoEvent.getUsername(), clientIp, address, loginInfoEvent.getMessage());

        SysLoginLog loginLog = SysLoginLog.builder()
                .tenantId(loginInfoEvent.getTenantId())
                .username(loginInfoEvent.getUsername())
                .clientName(loginInfoEvent.getClientName())
                .loginIp(clientIp)
                .loginLocation(address)
                .browser(browser)
                .os(os)
                .loginTime(loginInfoEvent.getLoginTime())
                .msg(loginInfoEvent.getMessage())
                .build();

        // 日志状态
        if (StringUtils.equalsAny(loginInfoEvent.getStatus(), AuthConstant.LOGIN_SUCCESS, AuthConstant.LOGOUT, AuthConstant.REGISTER)) {
            loginLog.setStatus(1);
        } else if (loginInfoEvent.getStatus().equals(AuthConstant.LOGIN_FAIL)) {
            loginLog.setStatus(0);
        }

        baseMapper.insert(loginLog);

        // 更新用户的登录记录
        SysUserDTO sysUserDTO = new SysUserDTO();
        sysUserDTO.setUsername(loginInfoEvent.getUsername())
                .setUserStatus(1)
                .setLoginIp(clientIp)
                .setLoginTime(LocalDateTime.now());

        sysUserService.updateByUserId(sysUserDTO);
    }

    @Override
    public List<SysLoginLogVO> listAll(SysLoginLogDTO sysLoginLogDTO) {
        LambdaQueryWrapper<SysLoginLog> wrapper = buildQueryWrapper(sysLoginLogDTO);
        List<SysLoginLog> sysLoginLogList = baseMapper.selectList(wrapper);
        return MapstructUtil.convert(sysLoginLogList, SysLoginLogVO.class);
    }

    @Override
    public TablePage<SysLoginLogVO> listPage(SysLoginLogDTO sysLoginLogDTO, PageQuery pageQuery) {
        LambdaQueryWrapper<SysLoginLog> wrapper = buildQueryWrapper(sysLoginLogDTO);

        if (Objects.isNull(pageQuery.getOrderRuleList())) {
            wrapper.orderByDesc(SysLoginLog::getLoginId);
        }

        Page<SysLoginLog> sysLoginLogPage = baseMapper.selectPage(pageQuery.buildPage(), wrapper);

        return TablePage.build(sysLoginLogPage, SysLoginLogVO.class);
    }

    private LambdaQueryWrapper<SysLoginLog> buildQueryWrapper(SysLoginLogDTO sysLoginLogDTO) {
        LambdaQueryWrapper<SysLoginLog> wrapper = Wrappers.<SysLoginLog>lambdaQuery()
                .like(StringUtil.hasText(sysLoginLogDTO.getUsername()), SysLoginLog::getUsername, sysLoginLogDTO.getUsername())
                .eq(StringUtil.hasText(sysLoginLogDTO.getClientName()), SysLoginLog::getClientName, sysLoginLogDTO.getClientName())
                .eq(StringUtil.hasText(sysLoginLogDTO.getBrowser()), SysLoginLog::getBrowser, sysLoginLogDTO.getBrowser())
                .eq(Objects.nonNull(sysLoginLogDTO.getStatus()), SysLoginLog::getStatus, sysLoginLogDTO.getStatus())
                .like(StringUtil.hasText(sysLoginLogDTO.getLoginIp()), SysLoginLog::getLoginIp, sysLoginLogDTO.getLoginIp())
                .like(StringUtil.hasText(sysLoginLogDTO.getLoginLocation()), SysLoginLog::getLoginLocation, sysLoginLogDTO.getLoginLocation());
        if (Objects.nonNull(sysLoginLogDTO.getLoginTime())) {
            wrapper.between(SysLoginLog::getLoginTime, sysLoginLogDTO.getLoginTime().get(0), sysLoginLogDTO.getLoginTime().get(1));
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




