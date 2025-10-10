package top.teek.encrypt.encryptor;

import top.teek.encrypt.core.EncryptContext;
import top.teek.encrypt.core.Encryptor;

/**
 * @author Teeker
 * @date 2024/6/8 22:41:33
 * @note
 */
public abstract class AbstractEncryptor implements Encryptor {

    public AbstractEncryptor(EncryptContext context) {
        // 用户配置校验与配置注入
    }
}
