package top.teek.uac.system.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 操作日志记录
 * @TableName t_sys_opera_log
 */
@Data
public class SysOperaLogDTO implements Serializable {

    /**
     * 模块标题
     */
    private String title;

    /**
     * 业务类型（0 其它 1 新增 2 修改 3 删除 ...）
     */
    private Integer businessType;

    /**
     * 操作人员
     */
    private String operaName;

    /**
     * 主机地址
     */
    private String operaIp;

    /**
     * 操作地点
     */
    private String operaLocation;

    /**
     * 状态（0 异常 1 正常 ）
     */
    private Integer status;

    /**
     * 操作时间
     */
    private List<String> operaTime;

    @Serial
    private static final long serialVersionUID = 1L;
}