package top.teek.integrate.system.model.bo;

import lombok.*;

/**
 * @author Teeker
 * @date 2024/10/28 00:49:24
 * @note
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigPoolBO {
    /**
     * 主键
     **/
    private Integer id;
    /**
     * 唯一标识符
     **/
    private String appId;
    /**
     * 系统名称
     **/
    private String appName;
    /**
     * 系统根地址
     **/
    private String url;
    /**
     * 调用系统请求方法：GET、POST 等
     **/
    private String method;
    /**
     * 传给系统的参数 * 格式：[{"name": "", "type": "normal", "value": ""}, {"name": "", "type": "dynamic", "value": ""}] * type 有 normal 和 dynamic 两种 * normal：value 是字符串 * dynamic：value 是时间动态参数，如 -2 就是基于当前时间减 2 天，+2 或 2 基于当前时间加 2天
     */
    private String reqParams;
    /**
     * 系统认证的参数
     **/
    private String authParams;
    /**
     * 系统系统认证地址
     **/
    private String authUrl;
    /**
     * 截取系统读取的数据
     **/
    private String interceptField;
    /**
     * 发送消息人
     **/
    private String mailTo;
    /**
     * 消息发送类型：EM 邮件、SM 短信、OA WeLink
     **/
    private String mailType;
    /**
     * 系统状态：0 启用，1 禁用
     **/
    private Integer status;
    /**
     * 系统查询范围参数，格式：xx - xxx，其中 xx 是开始时间，xxx 是结束时间
     **/
    private String rangeParams;
}