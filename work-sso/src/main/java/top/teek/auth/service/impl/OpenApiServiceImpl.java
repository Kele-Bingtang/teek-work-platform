package top.teek.auth.service.impl;

import top.teek.auth.convertor.SysClientConvertor;
import top.teek.auth.dto.OpenApiClientDTO;
import top.teek.auth.entity.SysAuthClient;
import top.teek.auth.mapper.OpenApiMapper;
import top.teek.auth.service.OpenApiService;
import top.teek.core.exception.ServiceException;
import top.teek.security.enumeration.AuthErrorCodeEnum;
import top.teek.security.enumeration.AuthGrantTypeEnum;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Teeker
 * @date 2023/9/24 2:46
 * @note
 */
@Service
public class OpenApiServiceImpl extends ServiceImpl<OpenApiMapper, SysAuthClient> implements OpenApiService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addApp(OpenApiClientDTO openApiClientDTO) {
        if (baseMapper.exists(Wrappers.<SysAuthClient>lambdaQuery().eq(SysAuthClient::getAppName, openApiClientDTO.getAppName()))) {
            throw new ServiceException(AuthErrorCodeEnum.AUTH_APP_NAME_IS_EXISTS.getCode(), AuthErrorCodeEnum.AUTH_APP_NAME_IS_EXISTS.getMessage());
        }
        if (baseMapper.exists(Wrappers.<SysAuthClient>lambdaQuery().eq(SysAuthClient::getAppCode, openApiClientDTO.getAppCode()))) {
            throw new ServiceException(AuthErrorCodeEnum.AUTH_APP_CODE_IS_EXISTS.getCode(), AuthErrorCodeEnum.AUTH_APP_CODE_IS_EXISTS.getMessage());
        }
        SysAuthClient authClient = SysClientConvertor.INSTANCE.convert(openApiClientDTO);
        authClient.setAppId(UUID.randomUUID().toString())
                .setAppSecret(UUID.randomUUID().toString())
                .setAppCode(openApiClientDTO.getAppCode().toLowerCase()) // 默认给与客户端模式和刷新令牌权限
                .setAuthorizedGrantTypes(Arrays.asList(AuthGrantTypeEnum.CLIENT_CREDENTIALS.getGrantType(), AuthGrantTypeEnum.REFRESH_TOKEN.getGrantType()));
    
        return save(authClient);
    }

    @Override
    public SysAuthClient getApp(String appId, String appSecret, String authorizedGrantType) {
        if (!StringUtil.hasText(appSecret)) {
            throw new ServiceException(AuthErrorCodeEnum.AUTH_APP_SECRET_ERROR.getCode(), AuthErrorCodeEnum.AUTH_APP_SECRET_ERROR.getMessage());
        }
        if (!StringUtil.hasText(authorizedGrantType)) {
            throw new ServiceException(AuthErrorCodeEnum.AUTH_APP_GRANT_TYPE_NOT_SUPPORT.getCode(), AuthErrorCodeEnum.AUTH_APP_GRANT_TYPE_NOT_SUPPORT.getMessage());
        }
        SysAuthClient oauthClient = getOne(Wrappers.<SysAuthClient>lambdaQuery().eq(SysAuthClient::getAppId, appId).eq(StringUtil.hasText(appSecret), SysAuthClient::getAppSecret, appSecret));
        if (Objects.isNull(oauthClient)) {
            throw new ServiceException(AuthErrorCodeEnum.AUTH_APP_GRANT_TYPE_NOT_REALIZE.getCode(), AuthErrorCodeEnum.AUTH_APP_GRANT_TYPE_NOT_REALIZE.getMessage());
        }
        if (oauthClient.getStatus() == 0) {
            throw new ServiceException(AuthErrorCodeEnum.AUTH_APP_STATUS_ENABLE.getCode(), AuthErrorCodeEnum.AUTH_APP_STATUS_ENABLE.getMessage());
        }
        if (!appSecret.equals(oauthClient.getAppSecret())) {
            throw new ServiceException(AuthErrorCodeEnum.AUTH_APP_SECRET_ERROR.getCode(), AuthErrorCodeEnum.AUTH_APP_SECRET_ERROR.getMessage());
        }
        if (Objects.isNull(oauthClient.getAuthorizedGrantTypes()) || !oauthClient.getAuthorizedGrantTypes().contains(authorizedGrantType)) {
            throw new ServiceException(AuthErrorCodeEnum.AUTH_APP_GRANT_TYPE_NOT_SUPPORT.getCode(), AuthErrorCodeEnum.AUTH_APP_GRANT_TYPE_NOT_SUPPORT.getMessage());
        }
        return oauthClient;
    }
}