package top.teek.ag.system.controller;

import top.teek.ag.system.model.dto.DataSourceDTO;
import top.teek.ag.system.model.dto.SqlDTO;
import top.teek.ag.system.model.vo.DataSourceTable;
import top.teek.ag.system.model.vo.DataSourceVO;
import top.teek.ag.system.permission.annotation.TeamAuthorize;
import top.teek.ag.system.service.DataSourceService;
import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.core.validate.RestGroup;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Teeker
 * @date 2024/6/30 21:07:46
 * @note
 */
@RestController
@RequestMapping("/dataSource")
@RequiredArgsConstructor
public class DataSourceController {
    private final DataSourceService dataSourceService;

    @GetMapping("/listPage")
    @Operation(summary = "数据源列表查询（分页）", description = "通过条件查询数据源列表（分页）")
    @TeamAuthorize(value = "#dataSourceDTO.getTeamId()", checkOwnerAndAdmin = true)
    public Response<TablePage<DataSourceVO>> listPage(@Validated(RestGroup.QueryGroup.class) DataSourceDTO dataSourceDTO, PageQuery pageQuery) {
        TablePage<DataSourceVO> dataSourceVOTablePage = dataSourceService.listPage(dataSourceDTO, pageQuery);
        return HttpResult.ok(dataSourceVOTablePage);
    }

    @GetMapping("/listSelect/{teamId}")
    @Operation(summary = "数据源下拉列表查询", description = "获取下拉框数据源列表")
    @TeamAuthorize(value = "#teamId", checkOwnerAndAdmin = true)
    public Response<List<DataSourceVO>> listSelect(@PathVariable String teamId) {
        List<DataSourceVO> dataSourceVOList = dataSourceService.listSelect(teamId);
        return HttpResult.ok(dataSourceVOList);
    }

    @GetMapping("/listByProjectId/{projectId}")
    @Operation(summary = "通过项目 ID 查询数据源列表", description = "通过项目 ID 查询数据源列表")
    public Response<List<DataSourceVO>> listByProjectId(@PathVariable String projectId) {
        List<DataSourceVO> dataSourceVOList = dataSourceService.listByProjectId(projectId);
        return HttpResult.ok(dataSourceVOList);
    }

    @PostMapping
    @Operation(summary = "数据源新增", description = "新增数据源")
    @TeamAuthorize(value = "#dataSourceDTO.getTeamId()", checkOwnerAndAdmin = true)
    public Response<Boolean> addDataSource(@Validated(RestGroup.AddGroup.class) @RequestBody DataSourceDTO dataSourceDTO) {
        if (dataSourceService.checkDataSourceNameUnique(dataSourceDTO)) {
            return HttpResult.failMessage("新增数据源「" + dataSourceDTO.getDataSourceName() + "」失败，数据源名称已存在");
        }
        return HttpResult.okOrFail(dataSourceService.addDataSource(dataSourceDTO));
    }

    @PutMapping
    @Operation(summary = "数据源修改", description = "修改数据源")
    @TeamAuthorize(value = "#dataSourceDTO.getTeamId()", checkOwnerAndAdmin = true)
    public Response<Boolean> editDataSource(@Validated(RestGroup.EditGroup.class) @RequestBody DataSourceDTO dataSourceDTO) {
        if (dataSourceService.checkDataSourceNameUnique(dataSourceDTO)) {
            return HttpResult.failMessage("新增数据源「" + dataSourceDTO.getDataSourceName() + "」失败，数据源名称已存在");
        }

        return HttpResult.okOrFail(dataSourceService.editDataSource(dataSourceDTO));
    }

    @DeleteMapping("/{dataSourceId}")
    @Operation(summary = "数据源删除", description = "删除数据源")
    public Response<Boolean> removeDataSource(@PathVariable String dataSourceId) {
        return HttpResult.okOrFail(dataSourceService.removeDataSource(dataSourceId));
    }

    @PostMapping("/testConnect")
    @Operation(summary = "数据源连接测试", description = "连接测试数据源")
    public Response<Boolean> testConnect(@RequestBody DataSourceDTO dataSourceDTO) {
        return HttpResult.okOrFail(dataSourceService.testConnect(dataSourceDTO));
    }

    @GetMapping("/listSchemaByDataSource/{dataSourceId}")
    @Operation(summary = "通过数据源 ID 查询数据源 Schema", description = "通过数据源 ID 查询数据源 Schema")
    public Response<List<String>> listSchemaByDataSource(@PathVariable String dataSourceId) {
        return HttpResult.ok(dataSourceService.listSchemaByDataSource(dataSourceId));
    }

    @GetMapping("/listTableBySchema/{dataSourceId}/{schema}")
    @Operation(summary = "通过 Schema 获取数据源 Table 列表", description = "通过 Schema 获取数据源 Table 列表")
    public Response<List<DataSourceTable>> listTableBySchema(@PathVariable String dataSourceId, @PathVariable String schema) {
        return HttpResult.ok(dataSourceService.listTableBySchema(dataSourceId, schema));
    }
    
    @PostMapping("/executeSelect")
    @Operation(summary = "执行查询语句", description = "执行查询语句")
    public Response<List<LinkedHashMap<String, Object>>> executeSelect(@Validated @RequestBody SqlDTO sqlDTO) {
        return HttpResult.ok(dataSourceService.executeSelect(sqlDTO));
    }

    @PostMapping("/generateTemple/{dataSourceId}/{schema}/{tableName}/{type}")
    @Operation(summary = "根据表名生成 SQL 模板", description = "根据表名生成 SQL 模板")
    public Response<String> generateTemple(@PathVariable String dataSourceId, @PathVariable String schema, @PathVariable String tableName, @PathVariable String type) {
        String result = dataSourceService.generateTemple(dataSourceId, schema, tableName, type);
        return HttpResult.ok(result);
    }
}
