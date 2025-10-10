package top.teek.ag.system.model.vo.component;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Teeker
 * @date 2024/7/21 18:03:37
 * @note
 */
@Data
@Accessors(chain = true)
public class ProTableColumnsVO {
    /**
     * 数据 key
     */
    private String prop;
    /**
     * 展示名称
     */
    private String label;
    /**
     * 表头宽度
     */
    private Integer width;
    /**
     * 列类型："index" | "selection" | "radio" | "expand" | "sort"
     */
    private String type;
    /**
     * 搜索项配置
     */
    private Search search = new Search();
    /**
     * 枚举类型（字典）
     */
    @JsonProperty("enum")
    private List<Map<String, Object>> enumMap;
    /**
     * 当前单元格值是否根据 enum 格式化（示例：enum 只作为搜索项数据，不参与内容格式化）
     */
    private boolean isFilterEnum = true;
    /**
     * 列对齐方式："left" | "center" | "right"
     */
    private String align = "center";

    @Data
    @Accessors(chain = true)
    public static class Search {
        /**
         * 搜索项组件类型
         */
        private String el;
        /**
         * 搜索项组件 props
         */
        private Map<String, Object> props = new HashMap<>();
        /**
         * 搜索项组件默认值
         */
        private String defaultValue;
    }
}
