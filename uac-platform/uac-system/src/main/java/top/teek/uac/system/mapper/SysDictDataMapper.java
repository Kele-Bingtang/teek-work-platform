package top.teek.uac.system.mapper;

import top.teek.uac.system.model.po.SysDictData;
import top.teek.uac.system.model.po.SysDictType;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-23-12 02:23:41
 * @note 针对表 t_sys_dict_data(字典数据表)的数据库操作 Mapper
*/
public interface SysDictDataMapper extends BaseMapper<SysDictData> {

    List<SysDictType> checkExitDictData(@Param(Constants.WRAPPER) Wrapper<SysDictData> queryWrapper);
}
