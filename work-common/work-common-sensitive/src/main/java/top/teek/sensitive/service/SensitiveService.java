package top.teek.sensitive.service;

/**
 * @author Teeker
 * @date 2024/6/8 01:29:43
 * @note 自定义敏感服务
 */
public interface SensitiveService {
    /**
     * 是否脱敏
     */
    boolean isSensitive(String roleKey, String perms);
}
