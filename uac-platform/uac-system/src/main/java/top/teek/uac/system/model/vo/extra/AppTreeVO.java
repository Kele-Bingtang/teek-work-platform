package top.teek.uac.system.model.vo.extra;

import top.teek.uac.system.model.po.SysApp;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Teeker
 * @date 2024/1/29 0:04
 * @note
 */
@Data
@AutoMapper(target = SysApp.class, convertGenerate = false)
public class AppTreeVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 应用 ID
     */
    private String appId;

    /**
     * 应用名
     */
    private String appName;

}
