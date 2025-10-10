package top.teek.uac.system.model.vo.extra;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Teeker
 * @date 2024/4/9 下午10:16
 * @note
 */
@Data
public class CacheInfo {
    /**
     * 缓存统计信息
     */
    private Properties info;

    /**
     * 缓存中可用键 Key 的总数
     */
    private Long dbSize;

    /**
     * 缓存完整信息
     */
    private List<Map<String, String>> commandStats;
}
