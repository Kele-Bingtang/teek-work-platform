package cn.youngkbt.ag.core.helper;

import cn.youngkbt.core.exception.ServiceException;
import cn.youngkbt.utils.StringUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Kele-Bingtang
 * @date 2024/7/7 22:53:00
 * @note
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SqlHelper {

    private static final XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
    private static final Configuration configuration = new Configuration();

    /**
     * 添加 LIMIT 到 SQL
     *
     * @param selectSql 查询 SQL
     * @param limit     limit 数量
     * @param dbType    数据源类型
     * @return 添加了 limit 的 SQL
     */
    public static String addLimitToSql(String selectSql, int limit, String dbType) {
        String finalSql = "";

        if ("MySQL".equalsIgnoreCase(dbType) || "MariaDB".equalsIgnoreCase(dbType) || "PostgreSQL".equalsIgnoreCase(dbType) || "SQLite".equalsIgnoreCase(dbType)) {
            finalSql = "SELECT * FROM (" + selectSql + ") t LIMIT " + limit;
        } else if ("Oracle".equalsIgnoreCase(dbType)) {
            // Oracle 使用 ROW_NUMBER() OVER() 来实现 LIMIT，注意这需要原 SQL 有明确的排序规则
            finalSql = "SELECT * FROM (SELECT t.*, ROW_NUMBER() OVER (ORDER BY some_column) AS rn FROM (" + selectSql + ") t) WHERE rn <= " + limit;
        } else if ("SQLServer".equalsIgnoreCase(dbType)) {
            finalSql = "SELECT TOP (" + limit + ") * FROM (" + selectSql + ") AS limiter";
        } else {
            throw new ServiceException("不支持数据源类型: " + dbType);
        }

        return finalSql;
    }

    /**
     * 处理 Mybatis 的参数 SQL，如 #{xxx}、${xxx}，处理为 ${p.xxx}、${p.xxx}，因为传给 Mabatis 的参数 @Params("p") Map
     *
     * @param originalSql 原始 SQL
     * @return 处理后的 SQL
     */
    public static String processMybatisParams(String originalSql) {
        // 检查是否包含 #{} 或者 ${}
        if (!originalSql.contains("#{") && !originalSql.contains("${")) {
            return originalSql;
        }

        // 正则表达式匹配 #{...} 和 ${...} 中的内容
        Pattern pattern = Pattern.compile("(#\\{([^}]*)}|\\$\\{([^}]*)})");
        Matcher matcher = pattern.matcher(originalSql);

        StringBuilder sql = new StringBuilder();
        while (matcher.find()) {
            String match = matcher.group(0);  // 完整匹配
            String param = matcher.group(2) != null ? matcher.group(2) : matcher.group(3); // 提取 {..} 中的内容
            if (!param.startsWith("p.")) {
                param = "p." + param;
            }

            String replacement = match.substring(0, 2) + param + match.substring(match.length() - 1);
            if (match.startsWith("${")) {
                replacement = replacement.replace("${", "@{");
            }
            matcher.appendReplacement(sql, replacement);
        }
        matcher.appendTail(sql);
        String finalSql = sql.toString();
        if (finalSql.contains("@{")) {
            finalSql = finalSql.replace("@{", "${");
        }
        return finalSql;
    }

    public static String getXmlSql(String sqlXml, String xmlId, Map<String, Object> sqlParam) {
        XPathParser xPathParser = null;
        List<XNode> xNodes = null;
        xPathParser = new XPathParser("<root>" + sqlXml + "</root>");
        XNode root = xPathParser.evalNode("root");
        xNodes = root.evalNodes("select");
        XNode xNode = xNodes.get(0);
        if (StringUtil.hasText(xmlId)) {
            for (XNode node : xNodes) {
                if (xmlId.equals(node.getStringAttribute("id"))) {
                    xNode = node;
                    break;
                }
            }
        }

        SqlSource sqlSource = xmlLanguageDriver.createSqlSource(configuration, xNode, null);
        return sqlSource.getBoundSql(sqlParam).getSql();
    }
}
