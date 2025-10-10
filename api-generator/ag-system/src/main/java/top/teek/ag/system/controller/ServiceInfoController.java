package top.teek.ag.system.controller;

import top.teek.ag.system.model.dto.ServiceInfoDTO;
import top.teek.ag.system.model.vo.ServiceInfoVO;
import top.teek.ag.system.permission.annotation.ProjectAuthorize;
import top.teek.ag.system.service.ServiceColService;
import top.teek.ag.system.service.ServiceInfoService;
import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.core.validate.RestGroup;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/6/25 23:24:21
 * @note
 */
@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class ServiceInfoController {

    private final ServiceInfoService serviceInfoService;
    private final ServiceColService serviceColService;

    @GetMapping("/getByServiceId/{serviceId}")
    @Operation(summary = "根据服务 ID 查询服务", description = "根据服务 ID 查询服务")
    public Response<ServiceInfoVO> getByServiceId(@PathVariable String serviceId) {
        ServiceInfoVO serviceInfoVO = serviceInfoService.getByServiceId(serviceId);
        return HttpResult.ok(serviceInfoVO);
    }

    @GetMapping("/listPage")
    @Operation(summary = "服务列表查询（分页）", description = "通过条件查询服务列表（分页）")
    @ProjectAuthorize(value = "#serviceInfoDTO.getProjectId()", checkRead = true)
    public Response<TablePage<ServiceInfoVO>> listPage(@Validated(RestGroup.QueryGroup.class) ServiceInfoDTO serviceInfoDTO, PageQuery pageQuery) {
        TablePage<ServiceInfoVO> serviceInfoVOTablePage = serviceInfoService.listPage(serviceInfoDTO, pageQuery);
        return HttpResult.ok(serviceInfoVOTablePage);
    }

    @GetMapping("/listSelectInProject/{projectId}/{serviceId}")
    @Operation(summary = "同一个项目服务列表查询）", description = "同一个项目服务列表查询")
    @ProjectAuthorize(value = "#projectId", checkRead = true)
    public Response<List<ServiceInfoVO>> listSelectInProject(@PathVariable String projectId, @PathVariable String serviceId) {
        List<ServiceInfoVO> serviceInfoVOList = serviceInfoService.listSelectInProject(projectId, serviceId);
        return HttpResult.ok(serviceInfoVOList);
    }

    @PostMapping
    @Operation(summary = "服务新增", description = "新增服务")
    @ProjectAuthorize(value = "#serviceInfoDTO.getProjectId()", checkReadAndWrite = true)
    public Response<Boolean> addService(@Validated(RestGroup.AddGroup.class) @RequestBody ServiceInfoDTO serviceInfoDTO) {
        if (serviceInfoService.checkServiceNameUnique(serviceInfoDTO)) {
            return HttpResult.failMessage("新增服务「" + serviceInfoDTO.getServiceName() + "」失败，服务名称已存在");
        }

        if (serviceInfoService.checkServiceUrlUnique(serviceInfoDTO)) {
            return HttpResult.failMessage("新增服务「" + serviceInfoDTO.getServiceName() + "」失败，服务链接已存在");
        }

        boolean addService = serviceInfoService.addService(serviceInfoDTO);
        return HttpResult.okOrFail(addService);
    }

    @PutMapping
    @Operation(summary = "服务修改", description = "修改服务")
    @ProjectAuthorize(value = "#serviceInfoDTO.getProjectId()", checkReadAndWrite = true)
    public Response<Boolean> editService(@Validated(RestGroup.EditGroup.class) @RequestBody ServiceInfoDTO serviceInfoDTO) {
        if (serviceInfoService.checkServiceNameUnique(serviceInfoDTO)) {
            return HttpResult.failMessage("编辑服务「" + serviceInfoDTO.getServiceName() + "」失败，服务名称已存在");
        }

        if (serviceInfoService.checkServiceUrlUnique(serviceInfoDTO)) {
            return HttpResult.failMessage("编辑服务「" + serviceInfoDTO.getServiceName() + "」失败，服务链接已存在");
        }

        boolean editService = serviceInfoService.editService(serviceInfoDTO);
        return HttpResult.okOrFail(editService);
    }

    @DeleteMapping("/{serviceId}")
    @Operation(summary = "服务删除", description = "删除服务")
    public Response<Boolean> removeService(@PathVariable String serviceId) {
        boolean removeService = serviceInfoService.removeService(serviceId);
        return HttpResult.okOrFail(removeService);
    }

    @PostMapping("/generateCol")
    @Operation(summary = "列配置项生成", description = "生成列配置项")
    @ProjectAuthorize(value = "#serviceInfoDTO.getProjectId()", checkReadAndWrite = true)
    public Response<String> generateCol(@Validated(RestGroup.OtherGroup.class) @RequestBody ServiceInfoDTO serviceInfoDTO) {
        if (serviceColService.checkExitCol(serviceInfoDTO.getServiceId())) {
            return HttpResult.failMessage("生成列配置项失败，列配置已生成");
        }
        Integer result = serviceInfoService.generateCol(serviceInfoDTO);
        return HttpResult.okMessage("生成了 " + result + " 个列配置项");
    }

}
