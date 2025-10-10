package top.teek.uac.system.model.vo;

import top.teek.excel.annotation.ExcelDictFormat;
import top.teek.excel.convert.ExcelDictConvert;
import top.teek.uac.system.export.NormalStatusHandler;
import top.teek.utils.TreeBuildUtil;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2023-11-12 00:21:11
 * @note 部门信息
*/
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDeptVO extends TreeBuildUtil.TreeBO<SysDeptVO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @ExcelProperty("序号")
    private Long id;
    
    /**
     * 部门 ID
     */
    @ExcelProperty("部门 ID")
    private String deptId;

    /**
     * 父级部门 ID
     */
    @ExcelProperty("父级部门 ID")
    private String parentId;

    /**
     * 父级部门名字
     */
    @ExcelIgnore
    private String parentName;

    /**
     * 祖级列表
     */
    @ExcelProperty("祖级列表")
    private String ancestors;

    /**
     * 部门名
     */
    @ExcelProperty("部门名")
    private String deptName;

    /**
     * 部门图标
     */
    @ExcelProperty("部门图标")
    private String icon;

    /**
     * 部门显示顺序
     */
    @ExcelProperty("部门显示顺序")
    private Integer orderNum;

    /**
     * 部门用户数量
     */
    @ExcelProperty("部门用户数量")
    private Integer userCount;

    /**
     * 部门负责人
     */
    @ExcelProperty("部门负责人")
    private String leader;

    /**
     * 负责电话
     */
    @ExcelProperty("负责电话")
    private String phone;

    /**
     * 邮箱
     */
    @ExcelProperty("邮箱")
    private String email;

    /**
     * 部门介绍
     */
    @ExcelProperty("部门介绍")
    private String intro;

    /**
     * 部门层级
     */
    @ExcelProperty("部门层级")
    private Integer level;

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

}