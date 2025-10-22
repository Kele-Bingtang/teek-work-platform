package top.teek.file.system.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2024/8/6 21:28:27
 * @note
 */
@Data
public class FileOperaLogVO {
    /**
     * 主键
     */
    private Long id;
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
     * 状态（1 正常 2 异常）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
