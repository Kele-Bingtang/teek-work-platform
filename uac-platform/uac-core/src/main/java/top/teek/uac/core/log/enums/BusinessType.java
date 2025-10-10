package top.teek.uac.core.log.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Teeker
 * @date 2024-21-27 16:21:41
 * @note 业务操作类型
 */
@Getter
@AllArgsConstructor
public enum BusinessType {
    /**
     * 其它
     */
    OTHER(0, "其它"),

    /**
     * 新增
     */
    INSERT(1, "新增"),

    /**
     * 修改
     */
    UPDATE(2, "修改"),

    /**
     * 删除
     */
    DELETE(3, "删除"),

    /**
     * 授权
     */
    GRANT(4, "授权"),

    /**
     * 导出
     */
    EXPORT(5, "导出"),

    /**
     * 导入
     */
    IMPORT(6, "导入"),

    /**
     * 清空数据
     */
    CLEAN(7, "清空数据");

    private final Integer value;
    private final String label;
}
