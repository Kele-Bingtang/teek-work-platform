package top.teek.uac.system.model.po;

import top.teek.mp.annotation.FieldValueFill;
import top.teek.mp.annotation.ValueStrategy;
import top.teek.mp.base.BaseDO;
import top.teek.uac.system.model.vo.SysAppVO;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author Teeker
 * @date 2023-20-12 00:20:10
 * @note 应用信息
*/
@TableName("t_sys_app")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysAppVO.class, reverseConvertGenerate = false)
public class SysApp extends BaseDO {
    /**
     * 应用 ID
     */
    @TableField(fill = FieldFill.INSERT)
    @FieldValueFill(ValueStrategy.SNOWFLAKE)
    private String appId;

    /**
     * 应用码
     */
    private String appCode;

    /**
     * 应用名
     */
    private String appName;
    
    /**
     * 应用类型
     */
    private String appType;

    /**
     * 应用介绍
     */
    private String intro;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 负责人 ID
     */
    private String ownerId;

    /**
     * 负责人 username
     */
    private String ownerName;

    /**
     * 租户编号
     */
    private String tenantId;


    /**
     * 客户端 ID
     */
    private String clientId;

}