package top.teek.ag.system.mapper;

import top.teek.ag.system.model.po.Project;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Teeker
 * @date 2024-18-22 00:18:33
 * @note 针对表「t_project（项目表）」的数据库操作 Mapper
 */
public interface ProjectMapper extends BaseMapper<Project> {

    List<Project> listProject(@Param(Constants.WRAPPER) Wrapper<Project> queryWrapper);
}




