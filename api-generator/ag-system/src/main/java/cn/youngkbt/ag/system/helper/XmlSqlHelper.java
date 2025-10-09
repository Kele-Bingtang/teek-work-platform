package cn.youngkbt.ag.system.helper;

import cn.youngkbt.utils.StringUtil;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.util.List;
import java.util.Map;

/**
 * @author Tianke
 * @date 2025/4/29 20:19:12
 * @since 1.0.0
 */
public class XmlSqlHelper {

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

        SqlSource sqlSource = new XMLLanguageDriver().createSqlSource(new Configuration(), xNode, null);
        return sqlSource.getBoundSql(sqlParam).getSql();
    }
}
