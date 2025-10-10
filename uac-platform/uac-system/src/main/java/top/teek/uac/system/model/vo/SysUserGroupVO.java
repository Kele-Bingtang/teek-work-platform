package top.teek.uac.system.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2024/3/13 0:01
 * @note
 */
@Data
public class SysUserGroupVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @ExcelProperty("序号")
    private Long id;

    /**
     * 用户组 ID
     */
    @ExcelProperty("用户组 ID")
    private String groupId;

    /**
     * 用户组名
     */
    @ExcelProperty("用户组名")
    private String groupName;

    /**
     * 用户组描述
     */
    @ExcelProperty("用户组描述")
    private String intro;

    /**
     * 负责人 ID
     */
    @ExcelProperty("负责人 ID")
    private String ownerId;

    /**
     * 负责人 username
     */
    @ExcelProperty("负责人 username")
    private String ownerName;

    /**
     * 应用 ID
     */
    @ExcelProperty("应用 ID")
    private String appId;

    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
