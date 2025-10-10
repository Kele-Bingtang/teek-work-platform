package top.teek.ag.system.permission.annotation;

import java.lang.annotation.*;

/**
 * @author Teeker
 * @date 2024/6/26 01:30:31
 * @note 项目权限注解，检查当前登录人是否有权限访问、操作项目。必须放在有项目 ID 的接口或者类上，如果获取不到项目 ID，则不进行权限校验
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProjectAuthorize {
    /**
     * SpEl 表达式，指定项目 ID
     */
    String value() default "";

    /**
     * 检查当前登录人是否拥有查看项目的权限（项目列表，和项目管理的服务、报表、类配置项等可读权限）
     */
    boolean checkRead() default false;

    /**
     * 检查当前登录人是否拥有操作项目内数据的权限（服务、报表、类配置项等增删改查权限），注：无法修改、删除项目本身
     */
    boolean checkReadAndWrite() default false;

    /**
     * 检查当前登录人是否为管理员（操作项目本身的权限，如修改、删除项目等）
     */
    boolean checkAdmin() default false;

    /**
     * 如果通过校验后，指定缓存时间，后面的请求在有效期内不需要校验。格式：1m、1h、1d 等，支持格式请看 #{@link org.springframework.boot.convert.DurationStyle.Unit}
     */
    String cacheTime() default "1h";
}
