package top.teek.core.constants;

/**
 * @author Teeker
 * @date 2023/11/13 23:09
 * @note 数据库字段常量
 */
public interface ColumnConstant {

    Integer STATUS_NORMAL = 1;
    Integer STATUS_EXCEPTION = 0;
    Integer NON_ISOLATE_AUTH = 0;
    Integer IS_ISOLATE_AUTH = 1;
    Integer NON_DELETED = 0;
    Integer DELETED = 1;
    String PARENT_ID = "0";
    /**
     * 菜单类型（目录）
     */
    String MENU_TYPE_DIR = "M";

    /**
     * 菜单类型（菜单）
     */
    String MENU_TYPE_MENU = "C";

    /**
     * 菜单类型（按钮）
     */
    String MENU_TYPE_BUTTON = "F";
}
