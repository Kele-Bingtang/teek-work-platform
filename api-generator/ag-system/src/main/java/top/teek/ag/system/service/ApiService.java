package top.teek.ag.system.service;

import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import jakarta.servlet.http.HttpServletResponse;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Teeker
 * @date 2024/7/7 17:24:38
 * @note
 */
public interface ApiService {
    /**
     * 获取一笔数据
     *
     * @param apiUri           服务完整地址
     * @param secretKey        秘钥
     * @param requestParamsMap 查询条件
     * @return 数据
     */
    LinkedHashMap<String, Object> getOne(String apiUri, String secretKey, Map<String, Object> requestParamsMap);

    /**
     * 获取数据列表
     *
     * @param apiUri           服务完整地址
     * @param secretKey        秘钥
     * @param requestParamsMap 查询条件
     * @param limit            限制条数
     * @return 数据列表
     */
    List<LinkedHashMap<String, Object>> list(String apiUri, String secretKey, Map<String, Object> requestParamsMap, Integer limit);

    /**
     * 获取分页数据
     *
     * @param apiUri           服务完整地址
     * @param secretKey        秘钥
     * @param requestParamsMap 查询条件
     * @param pageQuery        分页信息
     * @return 数据分页
     */
    TablePage<LinkedHashMap<String, Object>> page(String apiUri, String secretKey, Map<String, Object> requestParamsMap, PageQuery pageQuery);

    /**
     * 通过服务 ID 获取数据
     *
     * @param serviceId 服务 ID
     * @param requestParamsMap   查询条件
     * @return 数据列表
     */
    List<LinkedHashMap<String, Object>> listByServiceId(String serviceId, Map<String, Object> requestParamsMap);

    /**
     * 通过服务 ID 获取分页数据
     *
     * @param serviceId 服务 ID
     * @param pageQuery 分页信息
     * @return 报表数据
     */
    TablePage<LinkedHashMap<String, Object>> pageByServiceId(String serviceId, PageQuery pageQuery, Map<String, Object> requestParamsMap);

    /**
     * 导出数据
     *
     * @param serviceId 服务 ID
     * @param pageQuery 分页信息
     * @param response  响应对象
     */
    void export(String serviceId, PageQuery pageQuery, Map<String, Object> requestParamsMap, HttpServletResponse response);

    /**
     * 操作数据
     *
     * @param operateType 操作类型
     * @param apiUri      服务完整地址
     * @param secretKey   秘钥
     * @param dataMap     查询条件
     * @param jsonList    数据
     * @return 操作结果
     */
    Integer operate(String operateType, String apiUri, String secretKey, Map<String, Object> dataMap, List<Map<String, Object>> jsonList);

    /**
     * 通过服务 ID 操作数据
     *
     * @param operateType 操作类型
     * @param serviceId   服务 ID
     * @param dataMap     查询条件
     * @return 操作结果
     */
    Integer operateByServiceId(String operateType, String serviceId, Map<String, Object> dataMap);

}
