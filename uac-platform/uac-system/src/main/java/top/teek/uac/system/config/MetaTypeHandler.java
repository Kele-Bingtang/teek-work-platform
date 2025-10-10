package top.teek.uac.system.config;

import top.teek.uac.system.model.vo.router.Meta;
import top.teek.utils.JacksonUtil;
import top.teek.utils.StringUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Teeker
 * @date 2024/5/1 01:12:45
 * @note 菜单配置 META 数据库存储转为 JSON 串
 * @
 */
@MappedTypes(Meta.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class MetaTypeHandler extends BaseTypeHandler<Meta> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Meta parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JacksonUtil.toJsonStr(parameter));
    }

    @Override
    public Meta getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (!StringUtil.hasText(rs.getString(columnName))) {
            return null;
        }
        return JacksonUtil.toJson(rs.getString(columnName), Meta.class);
    }

    @Override
    public Meta getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (!StringUtil.hasText(rs.getString(columnIndex))) {
            return null;
        }
        return JacksonUtil.toJson(rs.getString(columnIndex), Meta.class);
    }

    @Override
    public Meta getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (!StringUtil.hasText(cs.getString(columnIndex))) {
            return null;
        }
        return JacksonUtil.toJson(cs.getString(columnIndex), Meta.class);
    } 
}
