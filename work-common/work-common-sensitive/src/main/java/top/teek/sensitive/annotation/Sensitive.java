package top.teek.sensitive.annotation;

import top.teek.sensitive.handler.SensitiveHandler;
import top.teek.sensitive.strategy.SensitiveStrategy;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Teeker
 * @date 2024/6/8 01:03:56
 * @note 数据脱敏注解，标注属性后，会创建一个 SensitiveHandler 的序列化器
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveHandler.class)
public @interface Sensitive {

    /**
     * 脱敏规则
     */
    SensitiveStrategy strategy();

    /**
     * UAC 角色码
     */
    String roleCode() default "";

    /**
     * UAC 菜单权限码
     */
    String perms() default "";

    /**
     * 前置不需要打码的长度，仅 strategy 为 SensitiveStrategy.CUSTOMIZE_RULE 生效
     */
    int startLen() default 0;

    /**
     * 后置不需要打码的长度，仅 strategy 为 SensitiveStrategy.CUSTOMIZE_RULE 生效
     */
    int endLen() default 0;

}
