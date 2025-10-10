package top.teek.ag.system.model.vo;

import top.teek.excel.annotation.ExcelDictFormat;
import top.teek.excel.convert.ExcelDictConvert;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2023-22-12 00:22:14
 * @note 字典类型
*/
@Data
public class DictTypeVO implements Serializable {

    /**
     * id
     */
    @ExcelProperty("序号")
    private Long id;
    
    /**
     * 字典主键
     */
    @ExcelProperty("字典主键")
    private String dictId;
    
    /**
     * 字典类型
     */
    @ExcelProperty("字典类型")
    private String dictCode;

    /**
     * 字典名称
     */
    @ExcelProperty("字典名称")
    private String dictName;

    /**
     * 是否开启级联（0 不开启，1 开启）
     */
    @ExcelProperty(value = "是否开启级联", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readExp = "0:不开启, 1:开启")
    private Integer isCascade;

    /**
     * 介绍
     */
    @ExcelProperty("介绍")
    private String intro;

    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Serial
    private static final long serialVersionUID = 1L;
}