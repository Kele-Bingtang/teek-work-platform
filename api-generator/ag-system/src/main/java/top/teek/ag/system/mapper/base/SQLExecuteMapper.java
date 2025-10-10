package top.teek.ag.system.mapper.base;

import top.teek.ag.system.model.bo.BatchOperateBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Teeker
 * @date 2024/6/30 01:12:19
 * @note
 */
@Mapper
public interface SQLExecuteMapper {
    /**
     * 查询对象列表
     *
     * @param sql 查询 sql
     * @return 查询出的数据
     */
    @Select("${sql}")
    List<LinkedHashMap<String, Object>> executeSelectList(@Param("sql") String sql, @Param("p") Map<String, Object> params);

    /**
     * 查询对象列表（分页）
     *
     * @param sql 查询 sql
     * @return 查询出的数据（分页）
     */
    @Select("${sql}")
    Page<LinkedHashMap<String, Object>> executeSelectPage(Page<LinkedHashMap<String, Object>> page, @Param("sql") String sql, @Param("p") Map<String, Object> params);

    /**
     * 新增操作
     *
     * @param tableName 表名
     * @param data      数据
     * @return 增删改操作成功条数
     */
    Integer executeInsert(@Param("tableName") String tableName, @Param("data") BatchOperateBO data);

    /**
     * 修改操作
     *
     * @param tableName 表名
     * @param data      数据
     * @return 增删改操作成功条数
     */
    Integer executeUpdate(@Param("tableName") String tableName, @Param("data") BatchOperateBO data);

    /**
     * 删除操作
     *
     * @param tableName 表名
     * @param data      数据
     * @return 增删改操作成功条数
     */
    Integer executeDelete(@Param("tableName") String tableName, @Param("data") BatchOperateBO data);

    /**
     * 批量新增操作
     *
     * @param tableName 表名
     * @param dataList  数据列表
     * @return 增删改操作成功条数
     */
    Integer executeInsertBatch(@Param("tableName") String tableName, @Param("dataList") List<BatchOperateBO> dataList);

    /**
     * 批量修改操作
     *
     * @param tableName 表名
     * @param dataList  数据列表
     * @return 增删改操作成功条数
     */
    Integer executeUpdateBatch(@Param("tableName") String tableName, @Param("dataList") List<BatchOperateBO> dataList);

    /**
     * 批量删除操作
     *
     * @param tableName 表名
     * @param dataList  数据列表
     * @return 增删改操作成功条数
     */
    Integer executeDeleteBatch(@Param("tableName") String tableName, @Param("dataList") List<BatchOperateBO> dataList);
}
