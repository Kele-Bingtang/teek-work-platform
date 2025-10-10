package top.teek.utils;

import com.alibaba.excel.annotation.ExcelIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Teeker
 * @date 2024/6/23 01:16:49
 * @note
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TreeBuildUtil extends cn.hutool.core.lang.tree.TreeUtil {

    public static <T extends TreeBO<T>> List<T> build(List<T> nodes, String parentId, Function<T, String> function) {
        Map<String, T> nodeMap = new HashMap<>();
        for (T tree : nodes) {
            nodeMap.put(function.apply(tree), tree);
        }

        for (T tree : nodes) {
            if (!parentId.equals(tree.getParentId())) { // 非根节点
                T parentNode = nodeMap.get(tree.getParentId());
                if (null != parentNode) {
                    parentNode.getChildren().add(tree);
                }
            }
        }

        List<T> roots = new ArrayList<>();
        for (T tree : nodes) {
            if (parentId.equals(tree.getParentId())) {
                roots.add(tree);
            }
        }

        return roots;
    }

    @Data
    public static class TreeBO<T> {
        private String parentId;
        @ExcelIgnore
        private List<T> children = new ArrayList<>();
    }

}
