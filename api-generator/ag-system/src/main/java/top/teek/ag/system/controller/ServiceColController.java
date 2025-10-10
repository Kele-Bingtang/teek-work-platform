package top.teek.ag.system.controller;

import top.teek.ag.system.model.dto.ServiceColBatchUpdateDTO;
import top.teek.ag.system.model.dto.ServiceColDTO;
import top.teek.ag.system.model.po.ServiceInfo;
import top.teek.ag.system.model.vo.ServiceColVO;
import top.teek.ag.system.model.vo.ServiceInfoVO;
import top.teek.ag.system.permission.annotation.ProjectAuthorize;
import top.teek.ag.system.service.ServiceColService;
import top.teek.ag.system.service.ServiceInfoService;
import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.core.validate.RestGroup;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.utils.ListUtil;
import top.teek.utils.MapstructUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/6/26 00:24:26
 * @note
 */
@RestController
@RequestMapping("/serviceCol")
@RequiredArgsConstructor
public class ServiceColController {

    private final ServiceColService serviceColService;
    private final ServiceInfoService serviceInfoService;
    
    @GetMapping("/listByServiceId/{serviceId}")
    @Operation(summary = "通过服务 ID 查询服务列配置项", description = "通过服务 ID 查询服务列配置项")
    public Response<List<ServiceColVO>> listByServiceId(@PathVariable String serviceId) {
        List<ServiceColVO> serviceColVOList = serviceColService.listByServiceId(serviceId);
        return HttpResult.ok(serviceColVOList);
    }

    @GetMapping("listPage")
    @Operation(summary = "服务列配置项查询（分页）", description = "通过条件查询服务列配置项（分页）")
    @ProjectAuthorize(value = "#serviceColDTO.getProjectId()", checkRead = true)
    public Response<TablePage<ServiceColVO>> listPage(@Validated(RestGroup.QueryGroup.class) ServiceColDTO serviceColDTO, PageQuery pageQuery) {
        TablePage<ServiceColVO> serviceColVOTablePage = serviceColService.listPage(serviceColDTO, pageQuery);
        return HttpResult.ok(serviceColVOTablePage);
    }

    @PostMapping
    @Operation(summary = "服务列配置项新增", description = "新增服务列配置项")
    @ProjectAuthorize(value = "#serviceColDTO.getProjectId()", checkReadAndWrite = true)
    public Response<Boolean> addServiceCol(@Validated(RestGroup.AddGroup.class) @RequestBody ServiceColDTO serviceColDTO) {
        if (serviceColService.checkServiceColUnique(serviceColDTO)) {
            return HttpResult.failMessage("新增列配置项失败，字段名称已存在");
        }

        boolean result = serviceColService.addServiceCol(serviceColDTO);
        return HttpResult.okOrFail(result);
    }

    @PutMapping
    @Operation(summary = "服务列配置项修改", description = "修改服务列配置项")
    @ProjectAuthorize(value = "#serviceColDTO.getProjectId()", checkReadAndWrite = true)
    public Response<Boolean> editServiceCol(@Validated(RestGroup.EditGroup.class) @RequestBody ServiceColDTO serviceColDTO) {
        if (serviceColService.checkServiceColUnique(serviceColDTO)) {
            return HttpResult.failMessage("修改列配置项失败，字段名称已存在");
        }

        boolean result = serviceColService.editServiceCol(serviceColDTO);
        return HttpResult.okOrFail(result);
    }

    @PutMapping("/editBatch")
    @Operation(summary = "服务列配置项批量修改", description = "批量修改服务列配置项")
    @ProjectAuthorize(value = "#batchUpdateDTO.getProjectId()", checkReadAndWrite = true)
    public Response<String> editServiceCol(@Validated @RequestBody ServiceColBatchUpdateDTO batchUpdateDTO) {
        if (ListUtil.isEmpty(batchUpdateDTO.getJsonColList())) {
            return HttpResult.errorMessage("批量修改的字段不能为空");
        }

        boolean result = serviceColService.editBatchServiceCol(batchUpdateDTO);
        return HttpResult.okOrFail(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "服务列配置项删除", description = "删除服务列配置项")
    public Response<Boolean> removeServiceCol(@PathVariable String id) {
        boolean result = serviceColService.removeServiceColById(id);
        return HttpResult.okOrFail(result);
    }

    @PostMapping("/reGenCol/{serviceId}")
    @Operation(summary = "重新生成新加入的列配置项", description = "重新生成新加入的列配置项")
    public Response<String> reGenCol(@PathVariable String serviceId) {
        // 获取服务信息
        ServiceInfoVO serviceInfoVO = serviceInfoService.getByServiceId(serviceId);
        ServiceInfo serviceInfo = MapstructUtil.convert(serviceInfoVO, ServiceInfo.class);

        Integer length = serviceColService.reGenCol(serviceInfo);
        if (length > 0) {
            return HttpResult.okMessage("更新了 " + length + " 条数据");
        } else {
            return HttpResult.okMessage("不需要更新，没有新增的字段");
        }
    }

    @PostMapping("/removeInvalidCol/{serviceId}")
    @Operation(summary = "删除不存在的列配置项", description = "删除不存在的列配置项")
    public Response<String> removeInvalidCol(@PathVariable String serviceId) {
        // 获取服务信息
        ServiceInfoVO serviceInfoVO = serviceInfoService.getByServiceId(serviceId);
        ServiceInfo serviceInfo = MapstructUtil.convert(serviceInfoVO, ServiceInfo.class);

        Integer length = serviceColService.removeInvalidCol(serviceInfo);
        if (length > 0) {
            return HttpResult.okMessage("删除了 " + length + " 条数据");
        } else {
            return HttpResult.okMessage("不需要删除，没有失效的字段");
        }
    }
}
