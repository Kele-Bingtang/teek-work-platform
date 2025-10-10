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

/**
 * @author Teeker
 * @date 2023/7/4 23:09
 * @note 将数据库的 varchar 类型转为 Java 的 List<String> 类型，或将 Java 的 List<String> 类型转为 varchar 类型
 */
@MappedJdbcTypes(JdbcType.NVARCHAR)
@MappedTypes(List.class)
public class ListStringTypeHandler extends BaseTypeHandler<List<String>> {

    private static final String DELIMITER = ",";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, String.join(DELIMITER, parameter));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseListString(rs.getString(columnName));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseListString(rs.getString(columnIndex));
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseListString(cs.getString(columnIndex));
    }

    private List<String> parseListString(String str) {
        if (!StringUtil.hasText(str)) {
            return Collections.emptyList();
        }
        // 将字符串按逗号分隔转换为 List<String>
        return Arrays.asList(str.split(DELIMITER));
    }

}