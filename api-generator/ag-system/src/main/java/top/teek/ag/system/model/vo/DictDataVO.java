package top.teek.ag.system.model.vo;

import top.teek.excel.annotation.ExcelDictFormat;
import top.teek.excel.convert.ExcelDictConvert;
import top.teek.utils.TreeBuildUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2023-21-12 00:21:40
 * @note 字典数据
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class DictDataVO extends TreeBuildUtil.TreeBO<DictDataVO> implements Serializable {
    
    /**
     * id
     */
    @ExcelProperty("序号")
    private Long id;

    /**
     * 字典数据 ID
     */
    @ExcelProperty("字典数据 ID")
    private String dataId;

    /**
     * 父级字典数据 ID
     */
    @ExcelProperty("父级字典数据 ID")
    private String parentId;
    
    /**
     * 字典标签
     */
    @ExcelProperty("字典标签")
    private String dictLabel;

    /**
     * 字典键值
     */
    @ExcelProperty("字典键值")
    private String dictValue;

    /**
     * 字典排序
     */
    @ExcelProperty("字典排序")
    private Integer dictSort;

    /**
     * tag 标签：el-tag | el-check-tag
     */
    @ExcelProperty("tag 标签")
    private String tagEl;

    /**
     * tag 类型：primary | success | info | warning | danger
     */
    @ExcelProperty("tag 类型")
    private String tagType;

    /**
     * tag 主题：dark | light | plain
     */
    @ExcelProperty("tag 主题")
    private String tagEffect;

    /**
     * tag 其他属性
     */
    @ExcelProperty("tag 其他属性")
    private String tagAttributes;

    /**
     * 是否默认（Y是 N否）
     */
    @ExcelProperty(value = "是否默认", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readExp = "Y:是, N:否")
    private String isDefault;

    /**
     * 字典编码
     */
    @ExcelProperty("字典编码")
    private String dictCode;

    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Serial
    private static final long serialVersionUID = 1L;
}