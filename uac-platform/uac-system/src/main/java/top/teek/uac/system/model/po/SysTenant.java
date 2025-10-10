package top.teek.uac.system.model.po;

import top.teek.mp.base.BaseDO;
import top.teek.uac.system.model.vo.SysTenantVO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2023-26-12 00:26:16
 * @note 租户表
*/
@TableName("t_sys_tenant")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysTenantVO.class)
public class SysTenant extends BaseDO {

    /**
     * 租户编号
     */
    private String tenantId;

    /**
     * 统一社会信用代码
     */
    private String licenseNumber;

    /**
     * 企业名
     */
    private String tenantName;

    /**
     * 企业图标
     */
    private String icon;

    /**
     * 企业所在地
     */
    private String address;

    /**
     * 企业用户数量（-1 不限制）
     */
    private Integer userCountCapacity;

    /**
     * 用户实际数量
     */
    private Integer userCount;

    /**
     * 企业创始人
     */
    private String founder;

    /**
     * 企业介绍
     */
    private String intro;

    /**
     * 联系人
     */
    private String contactUserName;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 企业域名
     */
    private String domain;

    /**
     * 租户过期时间（-1 无限期）
     */
    private LocalDateTime expireTime;
}