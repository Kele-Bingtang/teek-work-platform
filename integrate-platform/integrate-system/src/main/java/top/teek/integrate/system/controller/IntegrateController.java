package top.teek.integrate.system.controller;

import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.integrate.system.model.dto.SyncFlowGetDTO;
import top.teek.integrate.system.model.dto.SyncFlowPostDTO;
import top.teek.integrate.system.model.dto.SyncFlowReceiveDataDTO;
import top.teek.integrate.system.service.IntegrateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Teeker
 * @date 2024/10/28 00:21:16
 * @note
 */
@RestController
@RequestMapping("/integrate")
@Tag(name = "系统整合服务操作接口", description = "IntegrateController")
@Slf4j
@RequiredArgsConstructor
public class IntegrateController {
    private final IntegrateService integrateService;

    @GetMapping("/test")
    public Response<String> test() {
        return HttpResult.okMessage("Sis Test Request");
    }

    @GetMapping(value = "/getSourceAuth")
    @Operation(summary = "读取认证信息", description = "从 Source api 系统读取认证信息，然后返回响应")
    public Response<Object> getSourceAuth(@Validated SyncFlowGetDTO syncFlowGetDTO) {
        Object result = integrateService.getSourceAuth(syncFlowGetDTO);
        return HttpResult.ok(result);
    }

    @GetMapping(value = "/getSourceDataForGet")
    @Operation(summary = "读取数据", description = "从 Source api 系统读取数据，然后返回响应")
    public Response<Object> getSourceDataForGet(@Validated SyncFlowGetDTO syncFlowGetDTO) {
        Object result = integrateService.requestSourceForGet(syncFlowGetDTO);
        return HttpResult.ok(result);
    }

    @PostMapping("/getSourceDataForPost")
    @Operation(summary = "读取数据", description = "从 Source api 系统读取数据，然后返回响应")
    public Response<Object> getSourceDataForPost(@Validated @RequestBody SyncFlowPostDTO syncFlowPostDTO) {
        Object result = integrateService.requestSourceForPost(syncFlowPostDTO);
        return HttpResult.ok(result);
    }

    @PostMapping("/syncFlowForReceiveData")
    @Operation(summary = "开启数据集成流程", description = "接收 body 里的数据，传给 Target 系统，异步完成数据同步")
    public Response<Object> syncFlowForReceiveData(@RequestBody SyncFlowReceiveDataDTO syncFlowReceiveDataDTO) {
        Object result = integrateService.toTargetFlowForReceiveData(syncFlowReceiveDataDTO);
        return HttpResult.ok(result);
    }

    @GetMapping(value = "/syncFlowForGet")
    @Operation(summary = "开启数据集成流程", description = "从 Source API 系统读取数据，传给 Target 系统，异步完成数据同步")
    public Response<Object> syncFlowForGet(@Validated SyncFlowGetDTO syncFlowGetDTO) {
        Object result = integrateService.sourceToTargetFlowForGet(syncFlowGetDTO);
        return HttpResult.ok(result);
    }

    @PostMapping("/syncFlowForPost")
    @Operation(summary = "开启数据集成流程", description = "从 Source API 系统读取数据，传给 Target 系统，异步完成数据同步")
    public Response<Object> syncFlowForPost(@RequestBody SyncFlowPostDTO syncFlowPostDTO) {
        Object result = integrateService.sourceToTargetFlowForPost(syncFlowPostDTO);
        return HttpResult.ok(result);
    }
}