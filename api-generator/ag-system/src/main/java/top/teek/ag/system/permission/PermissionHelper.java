package top.teek.ag.system.permission;

import top.teek.ag.core.constant.CacheNameConstant;
import top.teek.ag.core.enums.ProjectMemberRole;
import top.teek.ag.core.enums.TeamMemberRole;
import top.teek.ag.system.service.ProjectMemberService;
import top.teek.ag.system.service.TeamMemberService;
import top.teek.cache.helper.CacheHelper;
import top.teek.core.exception.ServiceException;
import top.teek.helper.SpringHelper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2024/7/14 17:57:19
 * @note
 */
public class PermissionHelper {
    private static final TeamMemberService teamMemberService = SpringHelper.getBean(TeamMemberService.class);
    private static final ProjectMemberService projectMemberService = SpringHelper.getBean(ProjectMemberService.class);

    /**
     * 判断用户是否是项目管理员
     */
    public static boolean checkProjectAdmin(String userId, String projectId, String cacheTime) {
        String cacheName = CacheNameConstant.PREFIX + userId + "#" + cacheTime;

        String adminKey = projectId + ":admin";
        // 先从缓存读取，如果存在，则有权限
        if (Objects.nonNull(CacheHelper.get(cacheName, adminKey))) {
            return true;
        }

        if (!projectMemberService.checkMemberRole(userId, projectId, Collections.singletonList(ProjectMemberRole.ADMIN.ordinal()))) {
            throw new ServiceException("用户不是项目管理员，无法操作项目");
        } else {
            CacheHelper.put(cacheName, adminKey, true);
            return true;
        }
    }

    /**
     * 判断用户可以操作项目（项目成员）
     */
    public static boolean checkProjectOperator(String userId, String projectId, String cacheTime) {
        String cacheName = CacheNameConstant.PREFIX + userId + "#" + cacheTime;
        String readAndWriteKey = projectId + ":readAndWrite";
        // 先从缓存读取，如果存在，则有权限
        if (Objects.nonNull(CacheHelper.get(cacheName, readAndWriteKey))) {
            return true;
        }

        if (!projectMemberService.checkMemberRole(userId, projectId, List.of(ProjectMemberRole.ADMIN.ordinal(), ProjectMemberRole.MEMBER.ordinal()))) {
            throw new ServiceException("用户没有权限操作数据");
        } else {
            CacheHelper.put(cacheName, readAndWriteKey, true);
            return true;
        }
    }

    /**
     * 判断用户是否拥有项目读取权限
     */
    public static boolean checkProjectReader(String userId, String projectId, String cacheTime) {
        String cacheName = CacheNameConstant.PREFIX + userId + "#" + cacheTime;
        String readKey = projectId + ":read";
        // 先从缓存读取，如果存在，则有权限
        if (Objects.nonNull(CacheHelper.get(cacheName, readKey))) {
            return true;
        }

        if (!projectMemberService.checkMemberExist(userId, projectId) &&
                projectMemberService.checkMemberRole(userId, projectId, Collections.singletonList(ProjectMemberRole.PREVENT.ordinal()))) {
            throw new ServiceException("用户没有权限查看项目");
        } else {
            CacheHelper.put(cacheName, readKey, true);
            return true;
        }
    }

    /**
     * 团队所有者权限判断（团队信息编辑、项目解散，成员与团队权限配置、成员与项目权限配置等操作权限）
     */
    public static boolean checkTeamOwner(String userId, String teamId, String cacheTime) {
        String cacheName = CacheNameConstant.PREFIX + userId + "#" + cacheTime;
        String readKey = teamId + ":owner";
        // 先从缓存读取，如果存在，则有权限
        if (Objects.nonNull(CacheHelper.get(cacheName, readKey))) {
            return true;
        }
        if (!teamMemberService.checkMemberRole(teamId, userId, Collections.singletonList(TeamMemberRole.OWNER.ordinal()))) {
            throw new ServiceException("用户不是团队所有者，无法操作团队");
        } else {
            CacheHelper.put(cacheName, readKey, true);
            return true;
        }
    }

    /**
     * 团队所有者 & 管理员权限判断（项目新增权限、成员与项目权限配置等操作权限）
     */
    public static boolean checkTeamOwnerAndAdmin(String userId, String teamId, String cacheTime) {
        String cacheName = CacheNameConstant.PREFIX + userId + "#" + cacheTime;
        String readKey = teamId + ":ownerAndAdmin";
        // 先从缓存读取，如果存在，则有权限
        if (Objects.nonNull(CacheHelper.get(cacheName, readKey))) {
            return true;
        }
        if (!teamMemberService.checkMemberRole(teamId, userId, List.of(TeamMemberRole.OWNER.ordinal(), TeamMemberRole.ADMIN.ordinal()))) {
            throw new ServiceException("操作用户不是团队操作者，没有操作权限");
        } else {
            CacheHelper.put(cacheName, readKey, true);
            return true;
        }
    }
}
