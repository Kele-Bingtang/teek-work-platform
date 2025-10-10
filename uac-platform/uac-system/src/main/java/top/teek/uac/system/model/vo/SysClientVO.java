package top.teek.uac.system.model.vo;

import top.teek.excel.annotation.ExcelDictFormat;
import top.teek.excel.convert.ExcelDictConvert;
import top.teek.uac.system.export.NormalStatusHandler;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Teeker
 * @date 2023/11/26 22:49
 * @note
 */
@Data
public class SysClientVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty("序号")
    private Long id;

    /**
     * 客户端 ID
     */
    @ExcelProperty("客户端 ID")
    private String clientId;

    /**
     * 客户端 Key
     */
    @ExcelProperty("客户端 Key")
    private String clientKey;

    /**
     * 客户端名称
     */
    @ExcelProperty("客户端名称")
    private String clientName;

    /**
     * 客户端秘钥
     */
    @ExcelProperty("客户端秘钥")
    private String clientSecret;

    /**
     * 授权类型
     */
    @ExcelProperty("授权类型")
    private String grantTypes;

    /**
     * 授权类型
     */
    @ExcelProperty(value = "授权类型")
    private List<String> grantTypeList;

    /**
     * token 最低活跃频率时间，超出则 token 失效（-1 不限制，单位秒）
     */
    @ExcelProperty(value = "token 最低活跃频率时间")
    private Long activeTimeout;

    /**
     * token 有效期，超出则 token 失效，默认 12 小时（单位秒）
     */
    @ExcelProperty(value = "token 有效期")
    private Long timeout;

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
