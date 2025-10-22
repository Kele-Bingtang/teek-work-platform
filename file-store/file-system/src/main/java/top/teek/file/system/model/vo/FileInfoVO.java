package top.teek.file.system.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2024/8/6 21:27:53
 * @note
 */
@Data
public class FileInfoVO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 应用系统标识
     */
    private String appId;

    /**
     * 应用系统模块
     */
    private String appModule;

    /**
     * 附件唯一标识
     */
    private String fileKey;

    /**
     * 源附件名称
     */
    private String fileName;

    /**
     * 附件存储路径
     */
    private String filePath;

    /**
     * 附件类型
     */
    private String fileType;

    /**
     * 附件大小（KB）
     */
    private Long fileSize;

    /**
     * 失效时间
     */
    private LocalDateTime expireTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
