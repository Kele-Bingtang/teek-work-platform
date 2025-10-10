package top.teek.auth.service;

import top.teek.auth.dto.OpenApiClientDTO;
import top.teek.auth.entity.SysAuthClient;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Teeker
 * @date 2023/9/24 2:46
 * @note
 */
public interface OpenApiService extends IService<SysAuthClient> {
    boolean addApp(OpenApiClientDTO openApiClientDTO);

    SysAuthClient getApp(String appId, String appSecret, String authorizedGrantType);
}
