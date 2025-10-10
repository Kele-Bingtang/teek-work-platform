package top.teek.file.system.controller;

import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.core.validate.RestGroup;
import top.teek.file.system.aspect.app.annotation.AppAuthorize;
import top.teek.file.system.model.dto.FileInfoDTO;
import top.teek.file.system.model.vo.FileInfoVO;
import top.teek.file.system.service.FileInfoService;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/8/11 23:58:52
 * @note
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileInfoController {
    private final FileInfoService fileInfoService;

    @GetMapping("/listAppModule/{appId}")
    @Operation(summary = "AppModule 列表获取", description = "获取 AppModule 列表")
    public Response<List<FileInfoVO>> listAppModule(@PathVariable String appId) {
        List<FileInfoVO> appInfoVOList = fileInfoService.listAppModule(appId);
        return HttpResult.ok(appInfoVOList);
    }


    @GetMapping("/listPage")
    @Operation(summary = "文件列表列表查询（分页）", description = "通过条件查询文件列表（分页）")
    public Response<TablePage<FileInfoVO>> listPage(@Validated(RestGroup.QueryGroup.class) FileInfoDTO fileInfoDTO, PageQuery pageQuery) {
        TablePage<FileInfoVO> fileInfoVOList = fileInfoService.listPage(fileInfoDTO, pageQuery);
        return HttpResult.ok(fileInfoVOList);
    }

    @PostMapping
    @Operation(summary = "文件新增", description = "新增文件")
    public Response<Boolean> addFile(@RequestBody @Validated(RestGroup.AddGroup.class) FileInfoDTO fileInfoDTO) {
        boolean result = fileInfoService.addFile(fileInfoDTO);
        return HttpResult.ok(result);
    }

    @PutMapping
    @Operation(summary = "文件修改", description = "修改文件")
    public Response<Boolean> editFile(@RequestBody @Validated(RestGroup.EditGroup.class) FileInfoDTO fileInfoDTO) {
        boolean result = fileInfoService.editFile(fileInfoDTO);
        return HttpResult.ok(result);
    }

    @DeleteMapping("/{appId}/{fileKey}")
    @Operation(summary = "文件删除", description = "删除文件")
    @AppAuthorize("#appId")
    public Response<Boolean> removeFile(@PathVariable String appId, @PathVariable String fileKey) {
        boolean result = fileInfoService.removeFile(fileKey);
        return HttpResult.ok(result);
    }

    @DeleteMapping("/batch/{appId}")
    @Operation(summary = "文件删除", description = "批量删除文件")
    @AppAuthorize("#appId")
    public Response<Boolean> removeBatchFile(@PathVariable String appId, @RequestBody List<String> fileKeyList) {
        boolean result = fileInfoService.removeBatchFile(fileKeyList);
        return HttpResult.ok(result);
    }
}
