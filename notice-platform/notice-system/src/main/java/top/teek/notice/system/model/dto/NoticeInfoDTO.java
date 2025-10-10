package top.teek.notice.system.model.dto;

import top.teek.notice.system.model.po.NoticeInfo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/8/18 23:40:01
 * @note
 */
@Data
@AutoMapper(target = NoticeInfo.class, reverseConvertGenerate = false)
public class NoticeInfoDTO {
    /**
     * App ID
     */
    private String appId;
    /**
     * 接收者
     */
    private String to;
    /**
     * 抄送者
     */
    private String cc;
    /**
     * 密送者
     */
    private String bcc;
    /**
     * 发送人别称
     */
    private String fromName;
    /**
     * 主题
     */
    private String subject;
    /**
     * 正文
     */
    private String content;
    /**
     * 优先级
     */
    private Integer priority;
    /**
     * 分类
     */
    private String category;
    /**
     * 业务 ID
     */
    private String bizId;
    /**
     * 附件
     */
    private List<MultipartFile> fileList;
    /**
     * 是否显示自动提示
     */
    private Boolean showTip;
}
