package top.teek.ag.system.service;

import top.teek.ag.system.model.dto.DataSourceDTO;
import top.teek.ag.system.model.dto.SqlDTO;
import top.teek.ag.system.model.po.DataSource;
import top.teek.ag.system.model.vo.DataSourceTable;
import top.teek.ag.system.model.vo.DataSourceVO;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Teeker
 * @date 2024-06-30 21:00:59
 * @note 针对表「t_data_source（数据源配置表）」的数据库操作 Service
 */
public interface DataSourceService extends IService<DataSource> {

    /**
     * 通过条件查询数据源列表（分页）
     *
     * @param dataSourceDTO 数据源查询条件
     * @param pageQuery     分页信息
     * @return 数据源列表
     */
    TablePage<DataSourceVO> listPage(DataSourceDTO dataSourceDTO, PageQuery pageQuery);

    /**
     * 获取下拉框数据源列表
     *
     * @param teamId 团队 ID
     * @return 数据源列表
     */
    List<DataSourceVO> listSelect(String teamId);

    /**
     * 通过项目 ID 获取数据源列表
     *
     * @param projectId 项目 ID
     * @return 数据源列表
     */
    List<DataSourceVO> listByProjectId(String projectId);

    /**
     * 新增数据源
     *
     * @param dataSourceDTO 数据源信息
     * @return 是否新增成功
     */
    Boolean addDataSource(DataSourceDTO dataSourceDTO);

    /**
     * 修改数据源
     *
     * @param dataSourceDTO 数据源信息
     * @return 是否修改成功
     */
    Boolean editDataSource(DataSourceDTO dataSourceDTO);

    /**
     * 删除数据源
     *
     * @param dataSourceId 数据源 ID
     * @return 是否删除成功
     */
    Boolean removeDataSource(String dataSourceId);

    /**
     * 校验数据源名称是否唯一
     *
     * @param dataSourceDTO 数据源信息
     * @return 是否唯一
     */
    boolean checkDataSourceNameUnique(DataSourceDTO dataSourceDTO);

    /**
     * 连接测试数据源
     *
     * @param dataSourceDTO 数据源信息
     * @return 是否连接成功
     */
    boolean testConnect(DataSourceDTO dataSourceDTO);

    /**
     * 通过数据源 ID 获取数据源 Schema 列表
     *
     * @param dataSourceId 数据源 ID
     * @return 数据源 Schema 列表
     */
    List<String> listSchemaByDataSource(String dataSourceId);

    /**
     * 通过 Schema 获取数据源 Table 列表
     *
     * @param dataSourceId 数据源 ID
     * @param schema       Schema
     * @return 数据源 Table 列表
     */
    List<DataSourceTable> listTableBySchema(String dataSourceId, String schema);

    /**
     * 通过 Schema、Table 获取数据源 Column 列表
     *
     * @param dataSourceId 数据源 ID
     * @param schema       Schema
     * @param table        Table
     * @return 数据源 Column 列表
     */
    List<String> listColumnBySchema(String dataSourceId, String schema, String table);

    /**
     * 执行查询语句
     *
     * @param sqlDTO 查询语句
     * @return 查询结果
     */
    List<LinkedHashMap<String, Object>> executeSelect(SqlDTO sqlDTO);

    /**
     * 根据表名生成 SQL 模板
     *
     * @param dataSourceId 数据源 ID
     * @param schema Schema
     * @param tableName 表名
     * @param type      模板类型
     * @return 模板
     */
    String generateTemple(String dataSourceId, String schema, String tableName, String type);
}
