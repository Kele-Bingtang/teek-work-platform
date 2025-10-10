package top.teek.file.system.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2024/8/5 22:26:23
 * @note
 */
@Data
@Accessors(chain = true)
public class FileUploadSuccessVO {
    /**
     * 附件唯一标识
     */
    private String fileKey;

    /**
     * 源附件名称
     */
    private String fileName;

    /**
     * 失效时间
     */
    private LocalDateTime expireTime;
}
