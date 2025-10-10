package top.teek.ag.system.controller;

import top.teek.ag.core.constant.ColumnConstant;
import top.teek.ag.system.service.ApiService;
import top.teek.core.exception.ServiceException;
import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.utils.JacksonUtil;
import top.teek.utils.StringUtil;
import top.teek.web.annotation.RequestURI;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author Teeker
 * @date 2024/7/7 16:50:24
 * @note
 */
@RestController
@RequestMapping("/generic-api")
@RequiredArgsConstructor
public class ApiController {

    private final ApiService apiService;
    private static final String BASE_URI = "/generic-api";

    @GetMapping(value = "/one/**")
    @Operation(summary = "查询一笔数据", description = "查询一笔数据")
    public Response<LinkedHashMap<String, Object>> getOne(@RequestURI String requestUri, @RequestHeader(value = "Secret-Key", required = false) String secretKey, HttpServletRequest request) {
        // 获取请求参数
        Map<String, Object> requestParamsMap = getParameterMap(request);
        // 获取项目密钥（请求头或者请求参数里获取）
        String secretKeyParam = (String) requestParamsMap.get("secretKey");

        if (StringUtil.hasEmpty(secretKey, secretKeyParam)) {
            return HttpResult.failMessage("您没有权限访问");
        } else if (StringUtil.hasText(secretKeyParam)) {
            secretKey = secretKeyParam;
        }
        requestParamsMap.remove("secretKey");
        // 去掉固定的接口前缀，获取项目的实际 uri
        String apiUri = requestUri.substring((BASE_URI + "/one").length());
        LinkedHashMap<String, Object> one = apiService.getOne(apiUri, secretKey, requestParamsMap);
        return HttpResult.ok(one);
    }

    @GetMapping(value = "/list/**")
    @Operation(summary = "查询多笔数据", description = "查询多笔数据")
    public Response<List<LinkedHashMap<String, Object>>> list(@RequestURI String requestUri, @RequestHeader(value = "Secret-Key", required = false) String secretKey, HttpServletRequest request) {
        // 获取请求参数
        Map<String, Object> requestParamsMap = getParameterMap(request);
        // 获取项目密钥（请求头或者请求参数里获取）
        String secretKeyParam = (String) requestParamsMap.get("secretKey");

        if (StringUtil.hasEmpty(secretKey, secretKeyParam)) {
            return HttpResult.failMessage("您没有权限访问");
        } else if (StringUtil.hasText(secretKeyParam)) {
            secretKey = secretKeyParam;
        }
        requestParamsMap.remove("secretKey");
        // 去掉固定的接口前缀，获取项目的实际 uri
        String apiUri = requestUri.substring((BASE_URI + "/list").length());
        // 限制数据的数量
        String limit = (String) requestParamsMap.get("_limit");
        List<LinkedHashMap<String, Object>> list = apiService.list(apiUri, secretKey, requestParamsMap, Objects.nonNull(limit) ? Integer.valueOf(limit) : null);
        return HttpResult.ok(list);
    }

    @GetMapping(value = "/page/**")
    @Operation(summary = "查询分页数据", description = "查询分页数据")
    public Response<TablePage<LinkedHashMap<String, Object>>> page(@RequestURI String requestUri, @RequestHeader(value = "Secret-Key", required = false) String secretKey, HttpServletRequest request, PageQuery pageQuery) {
        // 获取请求参数
        Map<String, Object> requestParamsMap = getParameterMap(request);
        // 获取项目密钥（请求头或者请求参数里获取）
        String secretKeyParam = (String) requestParamsMap.get("secretKey");

        if (StringUtil.hasEmpty(secretKey, secretKeyParam)) {
            return HttpResult.failMessage("您没有权限访问");
        } else if (StringUtil.hasText(secretKeyParam)) {
            secretKey = secretKeyParam;
        }
        requestParamsMap.remove("secretKey");
        // 去掉固定的接口前缀，获取项目的实际 uri
        String apiUri = requestUri.substring((BASE_URI + "/page").length());
        TablePage<LinkedHashMap<String, Object>> list = apiService.page(apiUri, secretKey, requestParamsMap, pageQuery);

        return HttpResult.ok(list);
    }

    @GetMapping(value = "/listByServiceId/{serviceId}")
    @Operation(summary = "通过服务 ID 查询多笔数据", description = "通过服务 ID 查询多笔数据")
    public Response<List<LinkedHashMap<String, Object>>> listByServiceId(@PathVariable String serviceId, HttpServletRequest request) {
        Map<String, Object> dataMap = getParameterMap(request);
        List<LinkedHashMap<String, Object>> dataList = apiService.listByServiceId(serviceId, dataMap);
        return HttpResult.ok(dataList);
    }

    @GetMapping(value = "/pageByServiceId/{serviceId}")
    @Operation(summary = "通过服务 ID 查询多笔数据（分页）", description = "通过服务 ID 查询多笔数据（分页）")
    public Response<TablePage<LinkedHashMap<String, Object>>> pageByServiceId(@PathVariable String serviceId, PageQuery pageQuery, HttpServletRequest request) {
        Map<String, Object> dataMap = getParameterMap(request);
        TablePage<LinkedHashMap<String, Object>> dataList = apiService.pageByServiceId(serviceId, pageQuery, dataMap);
        return HttpResult.ok(dataList);
    }

    @PostMapping(value = "/export/{serviceId}")
    @Operation(summary = "导出分页数据", description = "导出分页数据")
    public void export(@PathVariable String serviceId, PageQuery pageQuery, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> requestParamsMap = getParameterMap(request);
        apiService.export(serviceId, pageQuery, requestParamsMap, response);
    }

    @PostMapping("/{operateType}/**")
    @Operation(summary = "操作数据", description = "操作数据")
    public Response<Integer> operate(@PathVariable String operateType, @RequestURI String requestUri, @RequestHeader(value = "Secret-Key", required = false) String secretKey, HttpServletRequest request) {
        // 获取请求参数
        Map<String, Object> dataMap = getParameterMap(request);
        // 获取项目密钥（请求头或者请求参数里获取）
        String secretKeyParam = (String) dataMap.get("secretKey");

        if (StringUtil.hasEmpty(secretKey, secretKeyParam)) {
            return HttpResult.failMessage("您没有权限访问");
        } else if (StringUtil.hasText(secretKeyParam)) {
            secretKey = secretKeyParam;
        }
        // 获取 body 的数据
        List<Map<String, Object>> jsonList = getJson(request);
        dataMap.remove("secretKey");
        // 去掉固定的接口前缀，获取项目的实际 uri
        String apiUri = requestUri.substring((BASE_URI + "/").length() + operateType.length());
        dataMap.remove("secretKey");
        Integer result = apiService.operate(operateType, apiUri, secretKey, dataMap, jsonList);

        // 新增返回主键
        if (ColumnConstant.INSERT.equalsIgnoreCase(operateType)) {
            return HttpResult.ok(result);
        }
        return HttpResult.okMessage(operateType + " 了 " + result + " 笔数据");
    }

    @PostMapping(value = "/operate/{operateType}/{serviceId}")
    @Operation(summary = "通过服务 ID 操作数据", description = "通过服务 ID 操作数据")
    public Response<Integer> operateByServiceId(@PathVariable String operateType, @PathVariable String serviceId, HttpServletRequest request) {
        Integer result = apiService.operateByServiceId(operateType, serviceId, getJson(request).get(0));
        return HttpResult.ok(result);
    }

    /**
     * 从请求里读取参数，转成 Map
     *
     * @param request 请求对象
     * @return 参数 Map
     */
    private Map<String, Object> getParameterMap(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>(16);
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (!paramValue.isEmpty()) {
                    map.put(paramName, paramValue);
                }
            } else if (paramValues.length > 0) {
                // 数组
                map.put(paramName, paramValues);
            }
        }
        return map;
    }

    /**
     * 获取 JSON 数据
     *
     * @param request 请求对象
     * @return JSON 数据
     */
    public List<Map<String, Object>> getJson(HttpServletRequest request) {
        StringBuilder sb;
        try {
            // 从前端获取输入字节流
            ServletInputStream requestInputStream = request.getInputStream();
            // 将字节流转换为字符流,并设置字符编码为 utf-8
            InputStreamReader ir = new InputStreamReader(requestInputStream, StandardCharsets.UTF_8);
            // 使用字符缓冲流进行读取
            BufferedReader br = new BufferedReader(ir);
            // 开始拼装 JSON 字符串
            String line;
            sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            throw new ServiceException("解析 JSON 失败");
        }
        String json = sb.toString();
        return json.startsWith("[") ? JacksonUtil.toListMap(json) : Collections.singletonList(JacksonUtil.toJson(json, Map.class));
    }
}
