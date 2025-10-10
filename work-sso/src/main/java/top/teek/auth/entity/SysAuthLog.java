package top.teek.auth.entity;

import top.teek.mp.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author Teeker
 * @date 2023/11/8 22:09
 * @note
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@TableName( "work_sso.t_sys_auth_log")
public class SysAuthLog extends BaseDO {
    /**
     * 应用 ID
     */
    @TableField("app_id")
    private Integer appId;
    /**
     * 用户 ID
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 日志内容
     */
    @TableField("content")
    private String content;
    /**
     * 用户状态：1 在线，2 离线
     */
    @TableField("user_status")
    private Integer userStatus;
}
