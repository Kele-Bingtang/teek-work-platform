package top.teek.uac.system.model.vo.extra;

import top.teek.uac.system.model.po.SysClient;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Teeker
 * @date 2024/3/9 14:35
 * @note
 */
@Data
@AutoMapper(target = SysClient.class, convertGenerate = false)
public class ClientTreeVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 应用 ID
     */
    private String clientId;

    /**
     * 应用名
     */
    private String clientName;
}
