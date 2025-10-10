package top.teek.ag.system.model.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author Teeker
 * @date 2024/8/4 15:38:33
 * @note
 */
@Data
@Builder
public class ProjectStatisticVO {
    /**
     * 服务数量
     */
    private Long serviceCount;
    /**
     * 目录数量
     */
    private Long categoryCount;
}
