package top.teek.integrate.system.handler;

import top.teek.core.http.Assert;
import top.teek.core.exception.ServerException;
import top.teek.core.instance.BeanInstance;
import top.teek.helper.SpringHelper;
import top.teek.integrate.system.helper.IntegrateUtil;
import top.teek.integrate.system.model.bo.ConfigPoolBO;
import top.teek.integrate.system.model.bo.SyncFlowBO;
import top.teek.integrate.system.service.ServiceHandlerAdapt;
import top.teek.utils.JacksonUtil;
import top.teek.utils.StringUtil;
import jakarta.validation.constraints.NotNull;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Teeker
 * @date 2024/10/28 00:26:25
 * @note
 */
public class DispatcherServiceHandler {
    private List<ServiceHandlerAdapt> serviceHandlerList;
    private final ReentrantLock lock = new ReentrantLock();

    public Object doDispatch(SyncFlowBO syncFlowBO, boolean sync) {
        if (Boolean.TRUE.equals(syncFlowBO.getOnlyGetAuth())) {
            return getSourceAuthInfo(syncFlowBO);
        }
        ConfigPoolBO sourceInfo = syncFlowBO.getSourceInfo();
        // GET 数据 
        if (!sync) {
            Object sourceData = getSourceData(syncFlowBO);
            if (dataIsNull(sourceData)) {
                return sourceData;
            }
            return getFinalData(sourceData, sourceInfo.getInterceptField());
        }
        // Sync 数据
        ConfigPoolBO targetInfo = syncFlowBO.getTargetInfo();
        Object sourceData = syncFlowBO.getSourceData();
        ServiceHandlerAdapt targetServiceHandler = searchServiceHandler(targetInfo);
        Object targetAuth = targetServiceHandler.auth(targetInfo, syncFlowBO.getTargetAuthInfo());
        if (Objects.nonNull(sourceInfo)) {
            sourceData = getSourceData(syncFlowBO);
        }
        if (dataIsNull(sourceData)) {
            return Collections.singletonList("获取系统数据失败，sourceData 数据为空");
        }
        Object response = targetServiceHandler.syncDataRequest(targetInfo, syncFlowBO.getTargetDeliverInfo(), Objects.nonNull(sourceInfo) ? getFinalData(sourceData, sourceInfo.getInterceptField()) : sourceData, targetAuth);
        List<Object> responseList = BeanInstance.newArrayList();
        responseList.add(response);

        // 第三方系统（暂时支持低代码平台） 
        List<ConfigPoolBO> extraPoolList = syncFlowBO.getExtraPoolBOList();
        if (Objects.nonNull(extraPoolList) && !extraPoolList.isEmpty()) {
            extraPoolList.forEach(extraInfo -> {
                ServiceHandlerAdapt extraPoolHandler = searchServiceHandler(extraInfo);
                // TODO 第三方系统支持认证 
                extraPoolHandler.auth(extraInfo, syncFlowBO.getTargetAuthInfo());
                Object res = extraPoolHandler.syncDataRequest(extraInfo, null, getFinalData(response, extraInfo.getInterceptField()), null);
                responseList.add(extraInfo.getAppName() + " 系统响应结果：" + res);
            });
        }
        return responseList;
    }

    private Object getSourceAuthInfo(SyncFlowBO syncFlowBO) {
        ConfigPoolBO sourceInfo = syncFlowBO.getSourceInfo();
        ServiceHandlerAdapt sourceServiceHandler = searchServiceHandler(sourceInfo);
        return sourceServiceHandler.auth(syncFlowBO.getSourceInfo(), syncFlowBO.getSourceAuthInfo());
    }

    private Object getSourceData(SyncFlowBO syncFlowBO) {
        ConfigPoolBO sourceInfo = syncFlowBO.getSourceInfo();
        ServiceHandlerAdapt sourceServiceHandler = searchServiceHandler(sourceInfo);
        Object auth = sourceServiceHandler.auth(syncFlowBO.getSourceInfo(), syncFlowBO.getSourceAuthInfo());
        return sourceServiceHandler.getData(sourceInfo, syncFlowBO.getSourceDeliverInfo(), auth);
    }

    public boolean dataIsNull(Object data) {
        if (Objects.isNull(data)) {
            return true;
        }
        if (data instanceof List) {
            List d = (List) data;
            Object o = d.get(0);
            if (d.isEmpty() || Objects.isNull(o)) {
                return true;
            }
            if (o instanceof Map) {
                Map map = (Map) o;
                // 低代码平台的数据
                if (map.isEmpty() || StringUtil.hasEmpty(String.valueOf(map.get(0)))) {
                    return true;
                }
            }
        }
        if (data instanceof Map) {
            Map map = (Map) data;
            if (map.isEmpty() || StringUtil.hasEmpty(String.valueOf(map.get(0)))) {
                return true;
            }
        }
        return false;
    }

    public Object getFinalData(Object originData, String interceptField) {
        if (originData instanceof String) {
            return processData((String) originData, interceptField);
        }
        if (originData instanceof Map || originData instanceof List) {
            return processData(JacksonUtil.toJsonStr(originData), interceptField);
        }
        throw new ServerException("data has incorrect format");
    }

    /**
     * 处理数据，转换成系统需要的数据，只处理 Map 类型数据 * * @param originData 数据 * @param interceptField 处理的格式模板 * @return 处理后的数据
     */
    public Object processData(String originData, String interceptField) {
        if (!JacksonUtil.canSerialize(originData)) {
            throw new ServerException("data has incorrect format");
        }
        if (StringUtil.hasEmpty(interceptField)) {
            if (originData.trim().startsWith("[")) {
                return JacksonUtil.toListMap(originData);
            }
            if (originData.trim().startsWith("{")) {
                return JacksonUtil.toJson(originData, Map.class);
            }
        }
        if (!JacksonUtil.canSerialize(interceptField)) {
            throw new ServerException("interceptField is not in JSON format");
        }
        // 假设 interceptFieldList 为 [{"nest": "data.row", "merge": "data.a"}, {"nest": "data.item", "merge": "data.b"}] 
        List<Map<String, Object>> interceptFieldList = JacksonUtil.toListMap(interceptField);
        Assert.nonNull(interceptFieldList, "interceptField is not in List JSON format");
        // Map 容器
        Map<String, Object> finalData = new HashMap<>(16);
        // List 容器 
        List<Object> objectList = new ArrayList<>();
        // List 数据值允许合并，不允许拆
        if (originData.trim().startsWith("[")) {
            List<Object> jsonMap = JacksonUtil.toListMap(originData);
            for (Map<String, Object> field : interceptFieldList) {
                String merge = (String) field.get("merge");
                // 合并成 merge 需要的数据 
                Object mergeData = IntegrateUtil.mergeData(jsonMap, merge);
                Map<String, Object> mergeDataMap = (Map) mergeData;
                finalData = IntegrateUtil.mergeAndrecursionMap(mergeDataMap, finalData);
            }
            if (!finalData.isEmpty() && objectList.isEmpty()) {
                return finalData;
            } else if (!objectList.isEmpty() && finalData.isEmpty()) {
                return objectList;
            }
        }
        // Map 数据可以拆分和合并 
        if (originData.trim().startsWith("{")) {
            Map<String, Object> jsonMap = JacksonUtil.toJson(originData, Map.class);
            for (Map<String, Object> field : interceptFieldList) {
                String nest = (String) field.get("nest");
                String merge = (String) field.get("merge");
                // 获取从 nest 读取的数据 
                Object value = IntegrateUtil.nestData(jsonMap, nest);
                // 合并成 merge 需要的数据 
                Object mergeData = IntegrateUtil.mergeData(value, merge);
                // 最终的数据可能是数组，也可能是 Map
                if (mergeData instanceof Map) {
                    Map<String, Object> mergeDataMap = (Map) mergeData;
                    finalData = IntegrateUtil.mergeAndrecursionMap(mergeDataMap, finalData);
                } else if (mergeData instanceof List) {
                    List<Object> mergeDataMap = (List) mergeData;
                    objectList.addAll(mergeDataMap);
                }
            }
            if (!finalData.isEmpty() && objectList.isEmpty()) {
                return finalData;
            } else if (!objectList.isEmpty() && finalData.isEmpty()) {
                return objectList;
            }
        }
        throw new ServerException("interceptField configuration is error");
    }

    public ServiceHandlerAdapt searchServiceHandler(@NotNull ConfigPoolBO config) {
        ServiceHandlerAdapt backupServiceHandler = null;
        for (ServiceHandlerAdapt serviceHandler : searchServiceHandlerAdaptList()) {
            if ("com.pxw.mit.sis.common.CommonService".equals(serviceHandler.getClass().getName())) {
                backupServiceHandler = serviceHandler;
            }
            if (serviceHandler.match(config)) {
                return serviceHandler;
            }
        }
        if (Objects.nonNull(backupServiceHandler)) {
            return backupServiceHandler;
        }
        throw new ServerException("no service handler adapt for process " + config.getAppName() + " System");
    }

    protected List<ServiceHandlerAdapt> searchServiceHandlerAdaptList() {
        if (null != serviceHandlerList) {
            return serviceHandlerList;
        }
        lock.lock();
        try {
            if (null != serviceHandlerList) {
                return serviceHandlerList;
            }
            Map<String, ServiceHandlerAdapt> beanOfType = SpringHelper.getBeansOfType(ServiceHandlerAdapt.class);
            serviceHandlerList = new ArrayList<>(beanOfType.values());
        } finally {
            lock.unlock();
        }
        return serviceHandlerList;
    }
}