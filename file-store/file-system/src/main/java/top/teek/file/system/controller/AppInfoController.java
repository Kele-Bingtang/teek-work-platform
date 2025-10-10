package top.teek.file.system.controller;

import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.core.validate.RestGroup;
import top.teek.file.system.model.dto.AppInfoDTO;
import top.teek.file.system.model.vo.AppInfoVO;
import top.teek.file.system.service.AppInfoService;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Teeker
 * @date 2024/8/11 20:34:29
 * @note
 */
@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
public class AppInfoController {

    private final AppInfoService appInfoService;

    @GetMapping("/listPage")
    @Operation(summary = "应用列表查询（分页）", description = "通过条件查询应用列表（分页）")
    public Response<TablePage<AppInfoVO>> listPage(@Validated(RestGroup.QueryGroup.class)AppInfoDTO appInfoDTO, PageQuery pageQuery) {
        TablePage<AppInfoVO> appInfoVOList = appInfoService.listPage(appInfoDTO, pageQuery);
        return HttpResult.ok(appInfoVOList);
    }

    @PostMapping
    @Operation(summary = "应用新增", description = "新增应用")
    public Response<Boolean> addApp(@RequestBody @Validated(RestGroup.AddGroup.class) AppInfoDTO appInfoDTO) {
        boolean result = appInfoService.addApp(appInfoDTO);
        return HttpResult.ok(result);
    }

    @PutMapping
    @Operation(summary = "应用修改", description = "修改应用")
    public Response<Boolean> editApp(@RequestBody @Validated(RestGroup.EditGroup.class) AppInfoDTO appInfoDTO) {
        boolean result = appInfoService.editApp(appInfoDTO);
        return HttpResult.ok(result);
    }

    @DeleteMapping("/{appId}")
    @Operation(summary = "应用删除", description = "删除应用")
    public Response<Boolean> removeApp(@PathVariable String appId) {
        boolean result = appInfoService.removeApp(appId);
        return HttpResult.ok(result);
    }
}
