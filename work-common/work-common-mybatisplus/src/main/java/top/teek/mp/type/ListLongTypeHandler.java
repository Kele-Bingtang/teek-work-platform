package top.teek.mp.type;

import top.teek.utils.StringUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Teeker
 * @date 2023/7/4 23:09
 * @note 将数据库的 varchar 类型转为 Java 的 List<Long> 类型，或将 Java 的 List<Long> 类型转为 varchar 类型
 */
@MappedJdbcTypes(JdbcType.NVARCHAR)
@MappedTypes(List.class)
public class ListLongTypeHandler extends BaseTypeHandler<List<Long>> {

    private static final String DELIMITER = ",";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Long> parameter, JdbcType jdbcType) throws SQLException {
        String collect = parameter.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(DELIMITER));
        ps.setString(i, collect);
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return stringToListLong(rs.getString(columnName));
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return stringToListLong(rs.getString(columnIndex));
    }

    @Override
    public List<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return stringToListLong(cs.getString(columnIndex));
    }

    private List<Long> stringToListLong(String columnValue) {
        if (!StringUtil.hasText(columnValue)) {
            return Collections.emptyList();
        }
        String[] values = columnValue.split(DELIMITER);
        return Arrays.stream(values)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}