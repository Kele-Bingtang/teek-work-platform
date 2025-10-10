package top.teek.ag.system.model.vo.component;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Teeker
 * @date 2024/7/21 18:09:50
 * @note
 */
@Data
public class ProFormSchemaVO {
    /**
     * 数据 key
     */
    private String prop;
    /**
     * 展示名称
     */
    private String label;
    /**
     * 组件类型
     */
    private String el;
    /**
     * 枚举类型（字典）
     */
    @JsonProperty("enum")
    private List<Map<String, Object>> enumMap;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * ElCol 的 props
     */
    private Map<String, Object> col = new HashMap<>();
    /**
     * 组件 props
     */
    private Map<String, Object> props = new HashMap<>();
}
