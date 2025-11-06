package top.teek.uac.system.model.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
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
 * @date 2023-12-12 00:24:19
 * @note 岗位信息
*/
@Data
public class SysPostVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    /**
     * id
     */
    @ExcelProperty("序号")
    private Long id;
    
    /**
     * 岗位 ID
     */
    @ExcelProperty("岗位 ID")
    private String postId;

    /**
     * 岗位编码
     */
    @ExcelProperty("岗位编码")
    private String postCode;

    /**
     * 岗位名称
     */
    @ExcelProperty("岗位名称")
    private String postName;

    /**
     * 显示顺序
     */
    @ExcelProperty("显示顺序")
    private Integer orderNum;

    /**
     * 岗位用户数量
     */
    @ExcelProperty("岗位用户数量")
    private Integer userCount;

    /**
     * 岗位介绍
     */
    @ExcelProperty("岗位介绍")
    private String intro;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(handler = NormalStatusHandler.class)
    private Integer status;

    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 部门 ID
     */
    @ExcelIgnore
    private String deptId;

}