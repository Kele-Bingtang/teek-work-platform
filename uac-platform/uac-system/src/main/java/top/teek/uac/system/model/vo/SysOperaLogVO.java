package top.teek.uac.system.model.vo;

import top.teek.excel.annotation.ExcelDictFormat;
import top.teek.excel.annotation.ExcelEnumFormat;
import top.teek.excel.convert.ExcelDictConvert;
import top.teek.excel.convert.ExcelEnumConvert;
import top.teek.uac.core.log.enums.BusinessType;
import top.teek.uac.system.export.NormalStatusHandler;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2024-06-23 01:10:02
 * @note 操作日志记录
*/
@Data
@AutoMapper(target = SysOperaLogVO.class, reverseConvertGenerate = false)
public class SysOperaLogVO implements Serializable {
    /**
     * 主键
     */
    @ExcelProperty("序号")
    private Long id;

    /**
     * 日志 ID
     */
    @ExcelProperty("日志 ID")
    private String operaId;

    /**
     * 模块标题
     */
    @ExcelProperty("模块标题")
    private String title;

    /**
     * 业务类型（0 其它 1 新增 2 修改 3 删除 ...）
     */
    @ExcelProperty(value = "业务类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readExp = "0:其它, 1:新增, 2:修改, 3:删除, 4:授权, 5:导出, 6:导入, 7:强退, 8:生成代码, 9:清空数据")
    private Integer businessType;

    /**
     * 方法名称
     */
    @ExcelProperty("方法名称")
    private String method;

    /**
     * 请求方式
     */
    @ExcelProperty("请求方式")
    private String requestMethod;

    /**
     * 操作类别（0 其它 1 后台用户 2 手机端用户）
     */
    @ExcelProperty(value = "操作类别", converter = ExcelEnumConvert.class)
    @ExcelEnumFormat(enumClass = BusinessType.class)
    private Integer operatorType;

    /**
     * 操作人员
     */
    @ExcelProperty("操作人员")
    private String operaName;

    /**
     * 部门名称
     */
    @ExcelProperty("部门名称")
    private String deptName;

    /**
     * 请求 URL
     */
    @ExcelProperty("请求 URL")
    private String operaUrl;

    /**
     * 主机地址
     */
    @ExcelProperty("主机地址")
    private String operaIp;

    /**
     * 操作地点
     */
    @ExcelProperty("操作地点")
    private String operaLocation;

    /**
     * 请求参数
     */
    @ExcelProperty("请求参数")
    private String operaParam;

    /**
     * 返回参数
     */
    @ExcelProperty("返回参数")
    private String jsonResult;

    /**
     * 错误消息
     */
    @ExcelProperty("错误消息")
    private String errorMsg;

    /**
     * 状态（0 异常 1 正常 ）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(handler = NormalStatusHandler.class)
    private Integer status;

    /**
     * 操作时间
     */
    @ExcelProperty("操作时间")
    private LocalDateTime operaTime;

    /**
     * 消耗时间
     */
    @ExcelProperty("消耗时间")
    private Integer costTime;

    @Serial
    private static final long serialVersionUID = 1L;
}