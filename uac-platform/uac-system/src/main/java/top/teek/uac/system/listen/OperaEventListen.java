package top.teek.uac.system.listen;

import top.teek.uac.core.log.event.OperaLogEvent;
import top.teek.uac.system.service.SysOperaLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Teeker
 * @date 2024/4/27 19:10:08
 * @note
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class OperaEventListen {

    private final SysOperaLogService sysOperaLogService;
    
    @Async
    @EventListener
    public void recordOperaLog(OperaLogEvent operaLogEvent) {
        sysOperaLogService.recordOperaLog(operaLogEvent);
    }
}
