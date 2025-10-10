package top.teek.encrypt.encryptor;

import top.teek.encrypt.core.EncryptContext;
import top.teek.encrypt.enumerate.AlgorithmType;
import top.teek.encrypt.enumerate.EncodeType;
import top.teek.encrypt.helper.EncryptHelper;

/**
 * @author Teeker
 * @date 2024/6/8 22:14:44
 * @note Base64 算法实现
 */
public class Base64Encryptor extends AbstractEncryptor {
    
    public Base64Encryptor(EncryptContext context) {
        super(context);
    }

    @Override
    public AlgorithmType algorithm() {
        return AlgorithmType.BASE64;
    }

    @Override
    public String encrypt(String value, EncodeType encodeType) {
        return EncryptHelper.encryptByBase64(value);
    }

    @Override
    public String decrypt(String value) {
        return EncryptHelper.decryptByBase64(value);
    }
}
