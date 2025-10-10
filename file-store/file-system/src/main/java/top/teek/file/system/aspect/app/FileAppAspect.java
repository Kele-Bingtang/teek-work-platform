package top.teek.file.system.aspect.app;

import top.teek.file.system.aspect.app.annotation.AppAuthorize;
import top.teek.file.system.helper.Assert;
import top.teek.file.system.mapper.AppInfoMapper;
import top.teek.file.system.model.po.AppInfo;
import top.teek.helper.SpElParserHelper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @author Teeker
 * @date 2024/8/11 16:48:42
 * @note
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class FileAppAspect {
    
    private final AppInfoMapper appInfoMapper;

    @Before("@annotation(appAuthorize)")
    public void before(JoinPoint joinPoint, AppAuthorize appAuthorize) {
        // 注解只能放在方法上，所以强转 MethodSignature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String appId = SpElParserHelper.parse(signature.getMethod(), joinPoint.getArgs(), appAuthorize.value(), String.class);
        Assert.hasText(appId, "AppId 不能为空");

        AppInfo appInfo = appInfoMapper.selectOne(Wrappers.<AppInfo>lambdaQuery()
                .eq(AppInfo::getAppId, appId));
        
        Assert.notNull(appInfo, "该 AppId 没有认证");
        Assert.isTrue(appInfo.getStatus() == 1, "该 AppId 已被禁用");
    }
}
