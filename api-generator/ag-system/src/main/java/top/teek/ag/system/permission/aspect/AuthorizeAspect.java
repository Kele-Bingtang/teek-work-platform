package top.teek.ag.system.permission.aspect;

import top.teek.ag.core.helper.AgHelper;
import top.teek.ag.core.helper.Assert;
import top.teek.ag.system.permission.PermissionHelper;
import top.teek.ag.system.permission.annotation.ProjectAuthorize;
import top.teek.ag.system.permission.annotation.TeamAuthorize;
import top.teek.ag.system.service.ProjectMemberService;
import top.teek.ag.system.service.TeamMemberService;
import top.teek.helper.SpElParserHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @author Teeker
 * @date 2024/6/26 01:54:46
 * @note 团队权限、项目权限切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizeAspect {

    private final TeamMemberService teamMemberService;
    private final ProjectMemberService projectMemberService;

    @Before("@annotation(projectAuthorize)")
    public void before(JoinPoint joinPoint, ProjectAuthorize projectAuthorize) {
        boolean checkRead = projectAuthorize.checkRead();
        boolean checkReadAndWrite = projectAuthorize.checkReadAndWrite();
        boolean checkAdmin = projectAuthorize.checkAdmin();

        if (!checkRead && !checkReadAndWrite && !checkAdmin) {
            return;
        }

        // 注解只能放在方法上，所以强转 MethodSignature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String projectId = SpElParserHelper.parse(signature.getMethod(), joinPoint.getArgs(), projectAuthorize.value(), String.class);

        Assert.hasText(projectId, "团队不存在");

        String cacheTime = projectAuthorize.cacheTime();
        String userId = AgHelper.getUserId();

        // 项目管理员权限判断（项目可读、可写权限、项目关联的服务、报表、类配置项的可读、可写权限）
        if (checkAdmin && PermissionHelper.checkProjectAdmin(userId, projectId, cacheTime)) {
            return;
        }

        // 项目成员权限判断（项目关联的服务、报表、类配置项的可读、可写权限）
        if (checkReadAndWrite && PermissionHelper.checkProjectOperator(userId, projectId, cacheTime)) {
            return;
        }

        // 项目可读权限判断（项目列表可读权限、项目关联的服务、报表、类配置项的可读权限）
        if (checkRead) {
            PermissionHelper.checkProjectReader(userId, projectId, cacheTime);
        }
    }

    @Before("@annotation(teamAuthorize)")
    public void before(JoinPoint joinPoint, TeamAuthorize teamAuthorize) {
        boolean checkOwner = teamAuthorize.checkOwner();
        boolean checkOwnerAndAdmin = teamAuthorize.checkOwnerAndAdmin();

        if (!checkOwner && !checkOwnerAndAdmin) {
            return;
        }

        // 注解只能放在方法上，所以强转 MethodSignature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String teamId = SpElParserHelper.parse(signature.getMethod(), joinPoint.getArgs(), teamAuthorize.value(), String.class);

        Assert.hasText(teamId, "团队不存在");

        String cacheTime = teamAuthorize.cacheTime();
        String userId = AgHelper.getUserId();

        // 团队所有者权限判断（团队信息编辑、项目解散，成员与团队权限配置、成员与项目权限配置等操作权限）
        if (checkOwner && PermissionHelper.checkTeamOwner(userId, teamId, cacheTime)) {
            return;
        }

        // 团队所有者 & 管理员权限判断（项目新增权限、成员与项目权限配置等操作权限）
        if (checkOwnerAndAdmin) {
            PermissionHelper.checkTeamOwnerAndAdmin(userId, teamId, cacheTime);
        }

    }
}
