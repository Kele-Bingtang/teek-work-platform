package top.teek.sensitive.handler;

import cn.hutool.core.util.StrUtil;
import top.teek.helper.SpringHelper;
import top.teek.sensitive.annotation.Sensitive;
import top.teek.sensitive.service.SensitiveService;
import top.teek.sensitive.strategy.SensitiveStrategy;
import top.teek.utils.StringUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2024/6/8 01:15:03
 * @note 脱敏处理器，实现 ContextualSerializer 初始化一个序列化器，继承 JsonSerializer 实现序列化功能
 */
@Slf4j
public class SensitiveHandler extends JsonSerializer<String> implements ContextualSerializer {
    private SensitiveStrategy strategy;
    private int startLen;
    private int endLen;
    private String roleCode;
    private String perms;

    /**
     * 执行序列化操作
     */
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        try {
            // 默认规则
            if (SensitiveStrategy.DEFAULT.equals(strategy)) {
                gen.writeString(value);
                return;
            }
            // 自定义规则
            if (SensitiveStrategy.CUSTOMIZE_RULE.equals(strategy)) {
                gen.writeString(StrUtil.hide(value, startLen, endLen));
                return;
            }
            // 如果传入了 roleCode 和 perms，代表走自定义 Service 实现类来进行校验，返回 true 开启脱敏
            SensitiveService sensitiveService = SpringHelper.getBean(SensitiveService.class);
            if (StringUtil.hasAnyText(roleCode, perms) && Objects.nonNull(sensitiveService)) {
                if (sensitiveService.isSensitive(roleCode, perms)) {
                    gen.writeString(strategy.getDesensitize().apply(value));
                } else {
                    // 返回 false 不序列化
                    gen.writeString(value);
                }
            } else {
                gen.writeString(strategy.getDesensitize().apply(value));
            }
        } catch (BeansException e) {
            log.error("脱敏实现不存在, 采用默认处理 => {}", e.getMessage());
            gen.writeString(value);
        }
    }

    /**
     * 项目初始化后，获取注解信息进行存储，每一个注解对应一个类实例
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        Sensitive annotation = property.getAnnotation(Sensitive.class);
        if (Objects.nonNull(annotation) && Objects.equals(String.class, property.getType().getRawClass())) {
            this.strategy = annotation.strategy();
            this.startLen = annotation.startLen();
            this.endLen = annotation.endLen();
            this.roleCode = annotation.roleCode();
            this.perms = annotation.perms();
            return this;
        }
        return prov.findValueSerializer(property.getType(), property);
    }
}
