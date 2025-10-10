package top.teek.ag.system.permission.annotation;

import java.lang.annotation.*;

/**
 * @author Teeker
 * @date 2024/6/26 01:32:48
 * @note 团队权限注解，检查当前登录人是否拥有操作团队数据的权限。必须放在有团队 ID 的接口或者类上，如果获取不到团队 ID，则不进行权限校验
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TeamAuthorize {
    /**
     * SpEl 表达式，指定项目 ID
     */
    String value() default "";

    /**
     * 检查当前登录人是否为团队所有者（团队删改、创建项目、邀请成员权限等）
     */
    boolean checkOwner() default false;

    /**
     * 检查当前登录人是否为团队所有者（团队删改、创建项目、邀请成员权限等）或管理员（创建项目、邀请成员权限等）
     */
    boolean checkOwnerAndAdmin() default false;
    
    /**
     * 如果通过校验后，指定缓存时间，后面的请求在有效期内不需要校验。格式：1m、1h、1d 等，支持格式请看 #{@link org.springframework.boot.convert.DurationStyle.Unit}
     */
    String cacheTime() default "1h";
}
