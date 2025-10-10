package top.teek.uac.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Teeker
 * @date 2024/6/14 23:41:00
 * @note
 */
@Getter
@AllArgsConstructor
public enum MenuType {
    CATALOG("C", "目录"),
    MENU("M", "菜单"),
    FUNCTION("F", "按钮");

    private final String value;
    private final String label;

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
