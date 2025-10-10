package top.teek.uac.core.helper;

import top.teek.security.domain.LoginUser;
import top.teek.security.utils.LoginHelper;
import top.teek.uac.core.constant.TenantConstant;

import java.util.Objects;
import java.util.Set;

/**
 * @author Teeker
 * @date 2024/6/8 15:06:51
 * @note UAC 操作 Redis 工具类
 */
public class UacHelper extends LoginHelper {

    static {
        LoginHelper.prefixCacheKey = "uac";
    }

    /**
     * 初始化 static 的代码
     */
    public static void init() {
    }

    public static String getTenantId() {
        LoginUser userInfo = getLoginUser();
        if (Objects.isNull(userInfo)) {
            return null;
        }
        return userInfo.getTenantId();
    }

    public static Set<String> getRoleCodes() {
        LoginUser userInfo = getLoginUser();
        if (Objects.isNull(userInfo)) {
            return null;
        }
        return userInfo.getRoleCodes();
    }

    public static Set<String> getMenuPermission() {
        LoginUser userInfo = getLoginUser();
        if (Objects.isNull(userInfo)) {
            return null;
        }
        return userInfo.getMenuPermission();
    }
    
    /**
     * 是否为管理员
     *
     * @return boolean
     */
    public static boolean isAdmin() {
        return isAdmin(getUserId());
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户 ID
     * @return boolean
     */
    public static boolean isAdmin(String userId) {
        return TenantConstant.DEFAULT_USER_ID.equals(userId);
    }

    /**
     * 是否为租户管理员
     *
     * @return boolean
     */
    public static boolean isTenantAdmin() {
        return isTenantAdmin(getRoleCodes());
    }

    /**
     * 是否为租户管理员
     *
     * @param rolePermission 角色权限标识组
     * @return boolean
     */
    public static boolean isTenantAdmin(Set<String> rolePermission) {
        if (Objects.isNull(rolePermission)) return false;
        return rolePermission.contains(TenantConstant.TENANT_ADMIN_ROLE_KEY);
    }

    public static boolean hasRole(String roleCode) {
        Set<String> roleCodes = getRoleCodes();
        if (Objects.isNull(roleCodes)) return false;

        return roleCodes.contains(roleCode);
    }

    public static boolean hasMenuPermission(String menuPermission) {
        Set<String> permission = getMenuPermission();
        if (Objects.isNull(permission)) return false;

        return permission.contains(menuPermission);
    }
}
