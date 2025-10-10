package top.teek.file.system.model.dto;

import top.teek.file.system.model.po.FileInfo;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2024/8/12 00:06:01
 * @note
 */
@Data
@AutoMapper(target = FileInfo.class, reverseConvertGenerate = false)
public class FileInfoDTO {
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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 失效时间
     */
    private LocalDateTime expireTime;

    /**
     * 是否失效
     */
    @AutoMapping(ignore = true, target = "expireTime")
    private Boolean expire;

}
