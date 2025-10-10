package top.teek.ag.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Teeker
 * @date 2024/6/24 23:25:24
 * @note
 */
@Getter
@AllArgsConstructor
public enum ProjectMemberRole {
    /**
     * 占位符
     */
    ZERO(""),
    ADMIN("管理员"),
    MEMBER("普通成员"),
    READONLY("只读成员"),
    PREVENT("禁止访问");

    private final String label;

    public static ProjectMemberRole getByOrdinal(Integer ordinal) {
        for (ProjectMemberRole value : values()) {
            if (value.ordinal() == ordinal) {
                return value;
            }
        }
        return null;
    }
}
