package top.teek.uac.system.model.vo;

import top.teek.excel.annotation.ExcelDictFormat;
import top.teek.excel.convert.ExcelDictConvert;
import top.teek.uac.system.export.NormalStatusHandler;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2023-12-12 00:24:45
 * @note 应用角色信息
 */
@Data
public class SysRoleVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    /**
     * id
     */
    @ExcelProperty("序号")
    private Long id;
    
    /**
     * 角色 ID
     */
    @ExcelProperty("角色 ID")
    private String roleId;

    /**
     * 角色码
     */
    @ExcelProperty("角色编码")
    private String roleCode;

    /**
     * 角色名
     */
    @ExcelProperty("角色名称")
    private String roleName;

    /**
     * 状态
     */
    @ExcelProperty(value = "角色状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(handler = NormalStatusHandler.class)
    private Integer status;

    /**
     * 显示顺序
     */
    @ExcelProperty("角色顺序")
    private Integer orderNum;

    /**
     * 角色介绍
     */
    @ExcelProperty("角色介绍")
    private String intro;

    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 应用 ID
     */
    @ExcelProperty("应用 ID")
    private String appId;

}