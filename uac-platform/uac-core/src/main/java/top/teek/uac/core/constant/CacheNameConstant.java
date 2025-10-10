package top.teek.uac.core.constant;

import top.teek.core.constants.GlobalConstant;

/**
 * @author Teeker
 * @date 2024/6/12 22:49:24
 * @note
 */
public interface CacheNameConstant {
    /**
     * 项目缓存前缀
     */
    String PREFIX = GlobalConstant.GLOBAL_REDIS_KEY + "uac:";

    /**
     * 数据字典
     */
    String SYS_DICT = PREFIX + "sys_dict";

    /**
     * 租户
     */
    String SYS_TENANT = PREFIX + "sys_tenant#30d";

}
