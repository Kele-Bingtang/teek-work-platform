package top.teek.integrate.system.context;

import top.teek.core.instance.BeanInstance;
import top.teek.integrate.system.model.bo.SyncFlowBO;

import java.util.Map;

/**
 * @author Teeker
 * @date 2024/10/28 21:27:49
 * @note
 */
public class ContextHolder {
    
    private static final ThreadLocal<SyncFlowBO> SYNC_FLOW_THREAD_LOCAL = ThreadLocal.withInitial(SyncFlowBO::new);
    private static final ThreadLocal<Map<String, Object>> MAP_THREAD_LOCAL = ThreadLocal.withInitial(BeanInstance::newHashMap);

    private ContextHolder() {
    }

    public static void set(String key, Object value) {
        Map<String, Object> map = getMap();
        map.put(key, value);
        MAP_THREAD_LOCAL.set(map);
    }

    public static void set(Map<String, Object> map) {
        MAP_THREAD_LOCAL.set(map);
    }

    public static SyncFlowBO getSyncFlow() {
        return SYNC_FLOW_THREAD_LOCAL.get();
    }

    public static void set(SyncFlowBO syncFlowBO) {
        SYNC_FLOW_THREAD_LOCAL.set(syncFlowBO);
    }

    public static Map<String, Object> getMap() {
        return MAP_THREAD_LOCAL.get();
    }

    public static Object getMap(String key) {
        return MAP_THREAD_LOCAL.get().get(key);
    }

    public static void remove() {
        removeMap();
        removeSyncFlow();
    }

    public static void removeMap() {
        MAP_THREAD_LOCAL.remove();
    }

    public static void removeSyncFlow() {
        SYNC_FLOW_THREAD_LOCAL.remove();
    }
}