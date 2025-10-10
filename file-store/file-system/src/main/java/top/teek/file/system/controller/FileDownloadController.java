package top.teek.file.system.controller;

import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.file.system.aspect.app.annotation.AppAuthorize;
import top.teek.file.system.aspect.log.annotation.OperateLog;
import top.teek.file.system.aspect.log.enums.OperatorType;
import top.teek.file.system.service.FileDownloadService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Teeker
 * @date 2024/8/6 21:17:04
 * @note
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/download")
public class FileDownloadController {

    private final FileDownloadService fileDownloadService;

    @GetMapping("/{appId}/{fileKey}")
    @Operation(summary = "文件预览", description = "预览文件（仅限文本类文件，如 md、txt。PPT、Word 等暂不支持预览）")
    @AppAuthorize("#appId")
    @OperateLog(operatorType = OperatorType.PREVIEW, appId = "#appId", fileKey = "#fileKey")
    public void preViewFileByFileKey(@PathVariable String appId, @PathVariable String fileKey, HttpServletResponse response) {
        fileDownloadService.downloadFileByFileKey(appId, fileKey, true, response);
    }

    @PostMapping("/{appId}/{fileKey}")
    @Operation(summary = "文件下载", description = "下载文件")
    @AppAuthorize("#appId")
    @OperateLog(operatorType = OperatorType.DOWNLOAD, appId = "#appId", fileKey = "#fileKey")
    public void downloadFileByFileKey(@PathVariable String appId, @PathVariable String fileKey, HttpServletResponse response) {
        fileDownloadService.downloadFileByFileKey(appId, fileKey, false, response);
    }

    @PostMapping("/base64/{appId}/{fileKey}")
    @Operation(summary = "文件下载", description = "获取图片的 Base64")
    @AppAuthorize("#appId")
    @OperateLog(operatorType = OperatorType.DOWNLOAD, appId = "#appId", fileKey = "#fileKey")
    public Response<String> getImageBase64(@PathVariable String appId, @PathVariable String fileKey) {
        String base64 = fileDownloadService.getImageBase64(appId, fileKey);
        return HttpResult.ok(base64);
    }
}
