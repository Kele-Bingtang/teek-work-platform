package top.teek.uac.auth.strategy.impl;

import top.teek.security.enumeration.AuthGrantTypeEnum;
import top.teek.uac.auth.strategy.AuthHandler;
import top.teek.uac.core.bo.LoginSuccessBO;
import top.teek.uac.core.bo.LoginUserBO;
import top.teek.uac.system.model.po.SysClient;

/**
 * @author Teeker
 * @date 2023/11/14 23:13
 * @note 授权码模式处理器
 */
public class AuthCodeAuthHandler implements AuthHandler {
    @Override
    public boolean support(AuthGrantTypeEnum grantType) {
        return false;
    }

    @Override
    public void validate(LoginUserBO loginUserBO) {

    }

    @Override
    public LoginSuccessBO login(LoginUserBO loginUserBO, SysClient sysClient) {
        return null;
    }
}
