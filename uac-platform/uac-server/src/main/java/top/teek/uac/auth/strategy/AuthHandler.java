package top.teek.uac.auth.strategy;

import top.teek.helper.SpringHelper;
import top.teek.security.enumeration.AuthErrorCodeEnum;
import top.teek.security.enumeration.AuthGrantTypeEnum;
import top.teek.uac.core.bo.LoginSuccessBO;
import top.teek.uac.core.bo.LoginUserBO;
import top.teek.uac.core.exception.AuthException;
import top.teek.uac.system.model.po.SysClient;

import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2023/11/14 0:14
 * @note 策略处理器
 */
public interface AuthHandler {
    String BASE_NAME = "AuthHandler";

    static LoginSuccessBO loginDispatch(LoginUserBO loginUserBO, SysClient sysClient) {
        // 第一种方法：循环扫描策略（谁 support 谁就是处理器）
        /*AuthGrantTypeEnum grantType = checkAuthGrantTypeThenGet(loginUserBO.getGrantType());
        AuthHandler instance = getAuthHandler(grantType);

        if (Objects.isNull(instance)) {
            throw new AuthException("授权类型不正确，无法找到处理 " + grantType.getGrantType() + " 的处理器");
        }
        
        instance.validate(loginUserBO);
        return instance.login(loginUserBO);*/
        
        // 第二种方法：约定 Bean 名策略（@Service 的 value 和 beanName 一致）
        checkAuthGrantTypeThenGet(loginUserBO.getGrantType());
        
        String beanName = loginUserBO.getGrantType() + BASE_NAME;
        if(!SpringHelper.containsBean(beanName)) {
            throw new AuthException("授权类型不正确!");
        }

        AuthHandler instance = SpringHelper.getBean(beanName);
        instance.validate(loginUserBO);
        return instance.login(loginUserBO, sysClient);
    }

    static AuthHandler getAuthHandler(AuthGrantTypeEnum grantType) {
        List<AuthHandler> authHandlerList = SpringHelper.getBeansByType(AuthHandler.class);
        for (AuthHandler authHandler : authHandlerList) {
            if (authHandler.support(grantType)) {
                return authHandler;
            }
        }
        return null;
    }
    
    static AuthGrantTypeEnum checkAuthGrantTypeThenGet(String grantType) {
        AuthGrantTypeEnum grantTypeEnum = AuthGrantTypeEnum.getByGrantType(grantType);
        if(Objects.isNull(grantTypeEnum)) {
            throw new AuthException(AuthErrorCodeEnum.AUTH_APP_GRANT_TYPE_NOT_REALIZE);
        }
        return grantTypeEnum;
    }

    /**
     * 处理器是否支持该授权类型
     */
    boolean support(AuthGrantTypeEnum grantType);

    /**
     * 校验该处理器能校验的分组内容
     */
    void validate(LoginUserBO loginUserBO);

    /**
     * 执行登录操作
     */
    LoginSuccessBO login(LoginUserBO loginUserBO, SysClient sysClient);
}
