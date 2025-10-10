package top.teek.auth.convertor;

import top.teek.core.base.BaseMapperConvertor;
import top.teek.auth.dto.OpenApiClientDTO;
import top.teek.auth.entity.SysAuthClient;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Teeker
 * @date 2023-41-24 02:41:38
 * @note 
*/
@Mapper
public interface SysClientConvertor extends BaseMapperConvertor<OpenApiClientDTO, SysAuthClient> {
    SysClientConvertor INSTANCE = Mappers.getMapper(SysClientConvertor.class);
}