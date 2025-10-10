package top.teek.file.system.aspect.log.aspect;

import top.teek.core.http.Response;
import top.teek.file.system.aspect.log.annotation.OperateLog;
import top.teek.file.system.model.po.FileOperaLog;
import top.teek.file.system.model.vo.FileUploadSuccessVO;
import top.teek.file.system.service.FileOperaLogService;
import top.teek.helper.SpElParserHelper;
import top.teek.utils.AddressUtil;
import top.teek.web.utils.ServletUtil;
import top.teek.utils.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2024/8/11 18:35:09
 * @note
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperaLogAspect {

    private final FileOperaLogService fileOperaLogService;

    /**
     * 计算操作消耗时间
     */
    private static final ThreadLocal<StopWatch> TIME_THREADLOCAL = new ThreadLocal<>();

    @Before("@annotation(operateLog)")
    public void before(JoinPoint joinPoint, OperateLog operateLog) {
        StopWatch stopWatch = new StopWatch();
        TIME_THREADLOCAL.set(stopWatch);
        stopWatch.start();
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(operateLog)", returning = "jsonResult")
    public void afterReturning(JoinPoint joinPoint, OperateLog operateLog, Object jsonResult) {
        handleLog(joinPoint, operateLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(operateLog)", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, OperateLog operateLog, Exception e) {
        handleLog(joinPoint, operateLog, e, null);
    }

    private void handleLog(final JoinPoint joinPoint, OperateLog operateLog, final Exception e, Object jsonResult) {
        try {
            FileOperaLog fileOperaLog = new FileOperaLog();
            fileOperaLog.setOperaType(operateLog.operatorType().ordinal());
            fileOperaLog.setStatus(1);
            fileOperaLog.setCreateTime(LocalDateTime.now());
            fileOperaLog.setOperaUser("System");
            fileOperaLog.setCreateBy("System");
            fileOperaLog.setCreateById("System");
            fileOperaLog.setUpdateBy("System");
            fileOperaLog.setUpdateById("System");

            // 请求的地址
            String ip = ServletUtil.getClientIp();
            fileOperaLog.setOperaIp(ip);
            fileOperaLog.setOperaLocation(AddressUtil.getRealAddressByIp(ip));

            HttpServletRequest request = ServletUtil.getRequest();
            if (Objects.nonNull(request)) {
                fileOperaLog.setOperaUrl(StringUtils.substring(request.getRequestURI(), 0, 255));
            }

            if (e != null) {
                fileOperaLog.setStatus(0);
                fileOperaLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }

            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            fileOperaLog.setMethod(className + "." + methodName + "()");

            // 注解只能放在方法上，所以强转 MethodSignature
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String appId = SpElParserHelper.parse(signature.getMethod(), joinPoint.getArgs(), operateLog.appId(), String.class);
            fileOperaLog.setAppId(appId);

            // 设置消耗时间
            StopWatch stopWatch = TIME_THREADLOCAL.get();
            stopWatch.stop();
            fileOperaLog.setCostTime(stopWatch.getTime());

            // fileKey
            List<FileOperaLog> fileOperaLogList = new ArrayList<>();
            String fileKey = operateLog.fileKey();
            if (StringUtil.hasEmpty(fileKey)) {
                if (((Response) jsonResult).getData() instanceof List jsonList) {
                    for (FileUploadSuccessVO fileUploadSuccessVO : (List<FileUploadSuccessVO>) jsonList) {
                        fileOperaLog.setFileKey(fileUploadSuccessVO.getFileKey());
                        fileOperaLogList.add(fileOperaLog);
                    }
                } else {
                    fileOperaLogList.add(fileOperaLog);
                }
            } else {
                fileKey = SpElParserHelper.parse(signature.getMethod(), joinPoint.getArgs(), fileKey, String.class);
                fileOperaLog.setFileKey(fileKey);
                fileOperaLogList.add(fileOperaLog);
            }

            fileOperaLogService.saveBatch(fileOperaLogList);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息:{}", exp.getMessage());
        } finally {
            TIME_THREADLOCAL.remove();
        }
    }
}
