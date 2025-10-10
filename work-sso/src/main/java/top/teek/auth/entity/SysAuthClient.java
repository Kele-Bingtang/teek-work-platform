package top.teek.auth.entity;

import top.teek.mp.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Teeker
 * @date 2023/9/24 2:43
 * @note
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "work_sso.t_sys_client", autoResultMap = true)
public class SysAuthClient extends BaseDO {
    /**
     * 应用 Code
     **/
    @TableField("app_code")
    private String appCode;
    /**
     * 应用名称
     **/
    @TableField("app_name")
    private String appName;
    /**
     * 应用 ID
     **/
    @TableField("app_id")
    private String appId;
    /**
     * 应用密钥
     **/
    @TableField("app_secret")
    private String appSecret;
    /**
     * 授权类型（模式）
     **/
    @TableField(value = "authorized_grant_types", typeHandler = JacksonTypeHandler.class)
    private List<String> authorizedGrantTypes;
}