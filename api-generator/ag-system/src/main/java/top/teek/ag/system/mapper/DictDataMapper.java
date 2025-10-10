package top.teek.ag.system.mapper;

import top.teek.ag.system.model.po.DictData;
import top.teek.ag.system.model.po.DictType;
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
public interface DictDataMapper extends BaseMapper<DictData> {

    List<DictType> checkExitDictData(@Param(Constants.WRAPPER) Wrapper<DictData> queryWrapper);
}
