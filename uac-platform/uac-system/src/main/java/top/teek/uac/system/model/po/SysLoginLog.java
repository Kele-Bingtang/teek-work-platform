package top.teek.uac.system.model.po;

import top.teek.mp.annotation.FieldValueFill;
import top.teek.mp.annotation.ValueStrategy;
import top.teek.uac.system.model.vo.SysLoginLogVO;
import com.baomidou.mybatisplus.annotation.*;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2023-22-12 00:22:36
 * @note 操作日志记
*/
@TableName("t_sys_login_log")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@AutoMapper(target = SysLoginLogVO.class, reverseConvertGenerate = false)
public class SysLoginLog implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 登录 ID
     */
    @TableField(fill = FieldFill.INSERT)
    @FieldValueFill(ValueStrategy.SNOWFLAKE)
    private String loginId;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 客户端名
     */
    private String clientName;

    /**
     * 登录 IP 地址
     */
    private String loginIp;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 提示消息
     */
    private String msg;

    /**
     * 状态（0 异常 1 正常 ）
     */
    private Integer status;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 租户编号
     */
    private String tenantId;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}