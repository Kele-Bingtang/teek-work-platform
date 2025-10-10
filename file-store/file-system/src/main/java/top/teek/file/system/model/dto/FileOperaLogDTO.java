package top.teek.file.system.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/8/6 21:27:05
 * @note
 */
@Data
public class FileOperaLogDTO {
    /**
     * 日志 ID
     */
    private String operaId;

    /**
     * 应用系统标识
     */
    private String appId;

    /**
     * 附件唯一标识
     */
    private String fileKey;

    /**
     * 方法名
     */
    private String method;

    /**
     * 操作类型（1 上传 2 下载）
     */
    private Integer operaType;

    /**
     * 操作用户
     */
    private String operaUser;

    /**
     * 主机地址
     */
    private String operaIp;

    /**
     * 操作地点
     */
    private String operaLocation;

    /**
     * 请求地址
     */
    private String operaUrl;

    /**
     * 消耗时间
     */
    private Long costTime;

    /**
     * 消耗时间
     */
    private String errorMsg;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private List<String> createTime;
}
