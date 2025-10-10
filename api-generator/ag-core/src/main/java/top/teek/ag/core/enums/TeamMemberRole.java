package top.teek.ag.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Teeker
 * @date 2024/6/22 15:20:04
 * @note 团队成员角色
 */
@Getter
@AllArgsConstructor
public enum TeamMemberRole {
    /**
     * 占位符
     */
    ZERO(""),
    OWNER("所有者"),
    ADMIN("管理员"),
    MEMBER("普通成员");
    
    private final String label;
    
}
