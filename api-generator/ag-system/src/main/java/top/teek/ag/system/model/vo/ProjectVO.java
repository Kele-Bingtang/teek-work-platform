package top.teek.ag.system.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author Teeker
 * @date 2024/6/24 22:36:18
 * @note
 */
@Data
public class ProjectVO implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 项目 ID
     */
    private String projectId;
    /**
     * 项目名
     */
    private String projectName;
    /**
     * 接口基础路径
     */
    private String baseUrl;
    /**
     * 项目描述
     */
    private String description;
    /**
     * 项目密钥，唯一
     */
    private String secretKey;
    /**
     * 数据源 ID
     */
    private List<String> dataSourceId;
    /**
     * 团队 ID
     */
    private String teamId;

    @Serial
    private static final long serialVersionUID = 1L;
}
