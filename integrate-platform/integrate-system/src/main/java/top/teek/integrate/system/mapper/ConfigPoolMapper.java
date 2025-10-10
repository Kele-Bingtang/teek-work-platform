package top.teek.integrate.system.mapper;

import top.teek.integrate.system.model.po.ConfigPool;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Teeker
 * @date 2024-10-28 00:45:43
 * @note 针对表【sis_tbl_config_pool】的数据库操作 Mapper
 */
public interface ConfigPoolMapper extends BaseMapper<ConfigPool> {

    @Select("<script>select stai.app_name, stai.root_domain, stcp.* from rpt_db.sis_tbl_config_pool stcp, rpt_db.sis_tbl_app_info stai where stcp.app_id = stai.id and stcp.id in " +
            "<foreach item='item' index='index' collection='ids' open='(' close=')' separator=','> #{item} </foreach></script>")
    List<ConfigPool> queryListJoinApp(@Param("ids") List<Integer> ids);

}




