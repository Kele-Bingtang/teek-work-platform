package top.teek.security.utils;

import top.teek.redis.constants.AuthRedisConstant;
import top.teek.redis.utils.RedisUtil;
import top.teek.security.domain.LoginUser;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * @author Teeker
 * @date 2024/1/25 1:28
 * @note
 */
public abstract class LoginHelper {

    public static String prefixCacheKey = "";

    public static boolean isLogin() {
        return RedisUtil.hasKey(prefixCacheKey + AuthRedisConstant.USER_INFO_KEY + SecurityUtils.getUsername());
    }

    public static LoginUser getLoginUser() {
        Object userInfo = RedisUtil.getForValue(prefixCacheKey + AuthRedisConstant.USER_INFO_KEY + SecurityUtils.getUsername());
        if (Objects.isNull(userInfo)) {
            return null;
        }
        return (LoginUser) userInfo;
    }

    public static LoginUser getLoginUser(String username) {
        Object userInfo = RedisUtil.getForValue(prefixCacheKey + AuthRedisConstant.USER_INFO_KEY + username);
        if (Objects.isNull(userInfo)) {
            return null;
        }
        return (LoginUser) userInfo;
    }

    public static List<LoginUser> getAllLoginUser() {
        List<LoginUser> loginUserList = new ArrayList<>();
        Set<String> keys = RedisUtil.keys(prefixCacheKey + AuthRedisConstant.USER_INFO_KEY + "*");
        for (String key : keys) {
            loginUserList.add((LoginUser) RedisUtil.getForValue(key));
        }
        return loginUserList;
    }

    public static void cacheUserInfo(LoginUser loginUser, Long timeout) {
        RedisUtil.setForValue(prefixCacheKey + AuthRedisConstant.USER_INFO_KEY + loginUser.getUsername(), loginUser, Duration.ofMillis(timeout));
    }

    public static boolean updateUserInfo(LoginUser loginUser) {
        String key = prefixCacheKey + AuthRedisConstant.USER_INFO_KEY + SecurityUtils.getUsername();
        // 单位为秒
        Long expire = RedisUtil.getExpire(key);
        // 获取过期时间
        Date date = new Date(Instant.now().toEpochMilli() + expire * 1000);
        RedisUtil.setForValue(prefixCacheKey + AuthRedisConstant.USER_INFO_KEY + loginUser.getUsername(), loginUser);
        return RedisUtil.expireAt(key, date);
    }

    public static String getUserId() {
        LoginUser userInfo = getLoginUser();
        if (Objects.isNull(userInfo)) {
            return null;
        }
        return userInfo.getUserId();
    }

    public static String getUsername() {
        LoginUser userInfo = getLoginUser();
        if (Objects.isNull(userInfo)) {
            return null;
        }
        return userInfo.getUsername();
    }


    public static boolean logout() {
        LoginUser userInfo = getLoginUser();
        if (Objects.isNull(userInfo)) {
            return false;
        }
        return logout(userInfo.getUsername());
    }

    public static boolean logout(String username) {
        return RedisUtil.delete(prefixCacheKey + AuthRedisConstant.USER_INFO_KEY + username);
    }
}
