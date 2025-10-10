package top.teek.uac.core.log.annotation;

import top.teek.uac.core.log.enums.BusinessType;
import top.teek.uac.core.log.enums.OperatorType;

import java.lang.annotation.*;

/**
 * @author Teeker
 * @date 2024/4/27 16:19:18
 * @note
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog {
    /**
     * 模块
     */
    String title() default "";

    /**
     * 功能
     */
    BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别
     */
    OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    boolean isSaveResponseData() default true;


    /**
     * 排除指定的请求参数
     */
    String[] excludeParamNames() default {};
}
