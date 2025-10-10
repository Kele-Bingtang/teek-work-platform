package top.teek.uac.auth.convertor;

import top.teek.core.base.BaseMapperConvertor;
import top.teek.uac.auth.model.vo.LoginVO;
import top.teek.uac.core.bo.LoginSuccessBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Teeker
 * @date 2023/11/16 0:45
 * @note
 */
@Mapper
public interface LoginBOToVOConvertor extends BaseMapperConvertor<LoginSuccessBO, LoginVO> {
    
    LoginBOToVOConvertor INSTANCE = Mappers.getMapper(LoginBOToVOConvertor.class);
}
