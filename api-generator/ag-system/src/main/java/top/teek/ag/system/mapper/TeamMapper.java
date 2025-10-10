package top.teek.ag.system.mapper;

import top.teek.ag.system.model.po.Team;
import top.teek.ag.system.model.vo.TeamRouteVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Teeker
 * @date 2024-23-22 00:23:21
 * @note 针对表「t_team（团队表）」的数据库操作 Mapper
 */
public interface TeamMapper extends BaseMapper<Team> {

    List<TeamRouteVO> listMyAllTeam(@Param("userId") String userId);
    
}




