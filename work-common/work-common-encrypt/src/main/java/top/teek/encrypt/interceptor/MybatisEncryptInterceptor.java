package top.teek.encrypt.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import top.teek.encrypt.annotation.EncryptField;
import top.teek.encrypt.core.EncryptContext;
import top.teek.encrypt.core.EncryptorManager;
import top.teek.encrypt.enumerate.AlgorithmType;
import top.teek.encrypt.enumerate.EncodeType;
import top.teek.encrypt.properties.EncryptorProperties;
import top.teek.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.*;

/**
 * @author Teeker
 * @date 2024/6/8 23:09:09
 * @note Mybatis 执行 insert、update 前对字段加密拦截器
 */
@Slf4j
@Intercepts({@Signature(
        type = ParameterHandler.class,
        method = "setParameters",
        args = {PreparedStatement.class})
})
@AllArgsConstructor
public class MybatisEncryptInterceptor implements Interceptor {

    private final EncryptorManager encryptorManager;
    
    private final EncryptorProperties defaultProperties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return invocation;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof ParameterHandler parameterHandler) {
            // 进行加密操作
            Object parameterObject = parameterHandler.getParameterObject();
            if (Objects.nonNull(parameterObject) && !(parameterObject instanceof String)) {
                encryptHandler(parameterObject);
            }
        }
        return target;
    }

    /**
     * 加密对象
     *
     * @param sourceObject 待加密对象
     */
    private void encryptHandler(Object sourceObject) {
        if (Objects.isNull(sourceObject)) {
            return;
        }
        if (sourceObject instanceof Map<?, ?> map) {
            new HashSet<>(map.values()).forEach(this::encryptHandler);
            return;
        }
        if (sourceObject instanceof List<?> list) {
            if(CollUtil.isEmpty(list)) {
                return;
            }
            // 判断第一个元素是否含有注解。如果没有直接返回，提高效率
            Object firstItem = list.get(0);
            if (Objects.isNull(firstItem) || CollUtil.isEmpty(encryptorManager.getFieldCache(firstItem.getClass()))) {
                return;
            }
            list.forEach(this::encryptHandler);
            return;
        }
        // 不在缓存中的类,就是没有加密注解的类（当然也有可能是 typeAliasesPackage 写错）
        Set<Field> fields = encryptorManager.getFieldCache(sourceObject.getClass());
        if(Objects.isNull(fields)){
            return;
        }
        try {
            for (Field field : fields) {
                field.set(sourceObject, encryptField(Convert.toStr(field.get(sourceObject)), field));
            }
        } catch (Exception e) {
            log.error("处理加密字段时出错", e);
        }
    }

    /**
     * 字段值进行加密。通过字段的批注注册新的加密算法
     *
     * @param value 待加密的值
     * @param field 待加密字段
     * @return 加密后结果
     */
    private String encryptField(String value, Field field) {
        if (Objects.isNull(value)) {
            return null;
        }
        EncryptField encryptField = field.getAnnotation(EncryptField.class);
        EncryptContext encryptContext = new EncryptContext();
        encryptContext.setAlgorithm(encryptField.algorithm() == AlgorithmType.DEFAULT ? defaultProperties.getAlgorithm() : encryptField.algorithm());
        encryptContext.setEncode(encryptField.encode() == EncodeType.DEFAULT ? defaultProperties.getEncode() : encryptField.encode());
        encryptContext.setPassword(!StringUtil.hasText(encryptField.password()) ? defaultProperties.getPassword() : encryptField.password());
        encryptContext.setPrivateKey(!StringUtil.hasText(encryptField.privateKey()) ? defaultProperties.getPrivateKey() : encryptField.privateKey());
        encryptContext.setPublicKey(!StringUtil.hasText(encryptField.publicKey()) ? defaultProperties.getPublicKey() : encryptField.publicKey());
        return this.encryptorManager.encrypt(value, encryptContext);
    }


    @Override
    public void setProperties(Properties properties) {
    }
}