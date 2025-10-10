package top.teek.ag.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Teeker
 * @date 2024/7/7 18:35:22
 * @note
 */
@AllArgsConstructor
@Getter
public enum QueryFilterType {

    NO_FILTER(0, ""),
    EQUAL(1, " = '{val}'"),
    NOT_EQUAL(2, " != '{val}'"),
    LEFT_LIKE(3, " LIKE '%{val}'"),
    RIGHT_LIKE(4, " LIKE '{val}%'"),
    LIKE(5, " LIKE '%{val}%'"),
    LESS_THAN(6, " < '{val}'"),
    GREATER_THAN(7, " > '{val}'"),
    LESS_EQUAL_THAN(8, " <= '{val}'"),
    GREATER_EQUAL_THAN(9, " >= '{val}'"),
    BETWEEN(10, " BETWEEN '{val0}' AND '{val1}'"),
    IN(11, "IN ({val})"),
    ;

    private final Integer index;
    private final String name;

    public static String getName(Integer index) {
        for (QueryFilterType queryFilterType : QueryFilterType.values()) {
            if (queryFilterType.getIndex().equals(index)) {
                return queryFilterType.getName();
            }
        }
        return null;
    }
}
