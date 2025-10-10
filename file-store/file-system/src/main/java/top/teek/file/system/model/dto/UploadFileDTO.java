package top.teek.file.system.model.dto;

import top.teek.core.validate.RestGroup;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/8/5 22:26:07
 * @note
 */
@Data
public class UploadFileDTO {
    /**
     * 应用系统标识
     */
    @NotBlank(message = "App Id （应用系统标识）能为空", groups = {RestGroup.AddGroup.class})
    private String appId;

    /**
     * 应用系统模块
     */
    @NotBlank(message = "App Module（应用系统模块）不能为空", groups = {RestGroup.AddGroup.class})
    private String appModule;

    /**
     * 源附件名称
     */
    private String fileName;

    /**
     * 上传用户 ID
     */
    private String uploadUserId;

    /**
     * 上传用户名
     */
    private String uploadUserName;

    /**
     * 文件过期时间
     */
    private Integer expireTime;

    /**
     * 图片 Base64
     */
    List<String> base64Images;
}
