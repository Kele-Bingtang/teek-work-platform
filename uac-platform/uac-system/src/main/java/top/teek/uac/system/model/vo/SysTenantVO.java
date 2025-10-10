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
 * @date 2023-12-12 00:26:16
 * @note 租户表
*/
@Data
public class SysTenantVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    /**
     * id
     */
    @ExcelProperty("序号")
    private Long id;

    /**
     * 租户编号
     */
    @ExcelProperty("租户编号")
    private String tenantId;

    /**
     * 统一社会信用代码
     */
    @ExcelProperty("统一社会信用代码")
    private String licenseNumber;

    /**
     * 企业名
     */
    @ExcelProperty("企业名")
    private String tenantName;

    /**
     * 企业图标
     */
    @ExcelProperty("企业图标")
    private String icon;

    /**
     * 企业所在地
     */
    @ExcelProperty("企业所在地")
    private String address;

    /**
     * 企业用户数量（-1 不限制）
     */
    @ExcelProperty("企业用户数量（-1 不限制）")
    private Integer userCountCapacity;

    /**
     * 用户实际数量
     */
    @ExcelProperty("用户实际数量")
    private Integer userCount;

    /**
     * 企业创始人
     */
    @ExcelProperty("企业创始人")
    private String founder;

    /**
     * 企业介绍
     */
    @ExcelProperty("企业介绍")
    private String intro;

    /**
     * 联系人
     */
    @ExcelProperty("联系人")
    private String contactUserName;

    /**
     * 联系电话
     */
    @ExcelProperty("联系电话")
    private String contactPhone;

    /**
     * 企业域名
     */
    @ExcelProperty("企业域名")
    private String domain;

    /**
     * 租户过期时间（-1 无限期）
     */
    @ExcelProperty("租户过期时间（-1 无限期）")
    private LocalDateTime expireTime;

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