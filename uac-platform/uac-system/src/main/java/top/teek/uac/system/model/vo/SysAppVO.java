package top.teek.uac.system.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import top.teek.excel.annotation.ExcelDictFormat;
import top.teek.excel.convert.ExcelDictConvert;
import top.teek.uac.system.export.NormalStatusHandler;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2023-12-12 00:20:10
 * @note 应用信息
 */
@Data
public class SysAppVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    /**
     * id
     */
    @ExcelProperty("序号")
    private Long id;
    /**
     * 应用 ID
     */
    @ExcelProperty("应用 ID")
    private String appId;

    /**
     * 应用码
     */
    @ExcelProperty("应用码")
    private String appCode;

    /**
     * 应用名
     */
    @ExcelProperty("应用名")
    private String appName;

    /**
     * 应用类型
     */
    @ExcelProperty(value = "应用类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readExp = "1:自研, 2:软件包")
    private String appType;

    /**
     * 应用介绍
     */
    @ExcelProperty("应用介绍")
    private String intro;

    /**
     * 显示顺序
     */
    @ExcelProperty("显示顺序")
    private Integer orderNum;

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
     * 状态
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(handler = NormalStatusHandler.class)
    private Integer status;

    /**
     * 更新人
     */
    @ExcelProperty("更新人")
    public String updateBy;

    /**
     * 更新人 id
     */
    @ExcelProperty("更新人 id")
    public String updateById;

    /**
     * 更新时间
     */
    @ExcelProperty("更新时间")
    public LocalDateTime updateTime;

    /**
     * 客户端 ID
     */
    @ExcelProperty("客户端 ID")
    private String clientId;
}