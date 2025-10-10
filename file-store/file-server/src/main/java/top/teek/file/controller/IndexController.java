package top.teek.file.controller;

import cn.hutool.core.util.StrUtil;
import top.teek.file.core.config.FileStoreConfig;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Teeker
 * @date 2024/8/5 00:54:55
 * @note
 */
@RequiredArgsConstructor
@RestController
public class IndexController {
    /**
     * 系统基础配置
     */
    private final FileStoreConfig fileStoreConfig;

    @GetMapping("/")
    @Operation(summary = "系统首页", description = "访问首页，返回提示语")
    public String index() {
        return StrUtil.format("欢迎使用 {}，当前版本：v{}，请通过前端地址访问。", fileStoreConfig.getName(), fileStoreConfig.getVersion());
    }
}
