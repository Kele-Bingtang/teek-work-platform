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
 * @date 2024-06-23 01:09:33
 * @note 操作日志记录
*/
@Data
public class SysLoginLogVO implements Serializable {
    /**
     * 主键
     */
    @ExcelProperty("序号")
    private Long id;
    
    /**
     * 登录 ID
     */
    @ExcelProperty("登录 ID")
    private String loginId;
    
    /**
     * 用户账号
     */
    @ExcelProperty("用户账号")
    private String username;

    /**
     * 客户端名
     */
    @ExcelProperty("客户端名")
    private String clientName;

    /**
     * 登录 IP 地址
     */
    @ExcelProperty("登录 IP 地址")
    private String loginIp;

    /**
     * 登录地点
     */
    @ExcelProperty("登录地点")
    private String loginLocation;

    /**
     * 浏览器类型
     */
    @ExcelProperty("浏览器类型")
    private String browser;

    /**
     * 操作系统
     */
    @ExcelProperty("操作系统")
    private String os;

    /**
     * 提示消息
     */
    @ExcelProperty("提示消息")
    private String msg;

    /**
     * 状态（0 异常 1 正常 ）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(handler = NormalStatusHandler.class)
    private Integer status;

    /**
     * 登录时间
     */
    @ExcelProperty("登录时间")
    private LocalDateTime loginTime;

    @Serial
    private static final long serialVersionUID = 1L;
}