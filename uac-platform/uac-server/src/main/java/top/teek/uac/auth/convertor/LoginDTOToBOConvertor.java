package top.teek.uac.auth.convertor;

import top.teek.uac.auth.model.dto.LoginUserDTO;
import top.teek.uac.core.bo.LoginUserBO;
import top.teek.uac.system.model.po.SysApp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Teeker
 * @date 2023/11/14 23:23
 * @note
 */
@Mapper
public interface LoginDTOToBOConvertor {
    LoginDTOToBOConvertor INSTANCE = Mappers.getMapper(LoginDTOToBOConvertor.class);

    @Mapping(source = "loginUserDTO.appId", target = "appId")
    @Mapping(source = "loginUserDTO.tenantId", target = "tenantId")
    LoginUserBO convert(LoginUserDTO loginUserDTO, SysApp sysApp);
}
