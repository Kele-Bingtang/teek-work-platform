package top.teek.mp.utils;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Teeker
 * @date 2023/7/4 23:10
 * @note
 */
@Slf4j
public class JSqlParseUtil {

    public static void main(String[] args) {
        String select1 = "SELECT * FROM a WHERE name like concat('%', ? ,'%') AND ((a = 1) and (c=2 or d = 1))";
        String select2 = "SELECT * FROM a WHERE name like concat('%', ? ,'%')";
        String select3 = "SELECT * FROM a WHERE name like concat('%', ? ,'%') OR a like concat('%', ? ,'%') AND c = 3";
        String select4 = "SELECT * FROM a WHERE a =2 AND c = 3 OR name  like concat('%', ? ,'%') ";
        String select5 = "SELECT * FROM a WHERE a =2 AND c = 3";
        String update5 = "SELECT * FROM a WHERE a =2 AND c = 3";
        Set<String> likeFields = getLikeField(select3);
        System.out.println(likeFields);
    }

    public static Expression getLeftExpression(Expression expression) {
        if (expression instanceof OrExpression) {
            OrExpression orExpression = (OrExpression) expression;
            return orExpression.getLeftExpression();
        } else if (expression instanceof AndExpression) {
            AndExpression andExpression = (AndExpression) expression;
            return andExpression.getLeftExpression();
        }
        return null;
    }

    public static Expression getRightExpression(Expression expression) {
        if (expression instanceof OrExpression) {
            OrExpression orExpression = (OrExpression) expression;
            return orExpression.getRightExpression();
        } else if (expression instanceof AndExpression) {
            AndExpression andExpression = (AndExpression) expression;
            return andExpression.getRightExpression();
        }
        return null;
    }

    public static Set<String> getLikeField(String sql) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            if (statement instanceof Select) {
                Set<String> likeFields = new HashSet<>();
                PlainSelect plainSelect = (PlainSelect) ((Select) statement).getSelectBody();
                getLikeField(plainSelect.getWhere(), likeFields);
                return likeFields;
            }
        } catch (JSQLParserException e) {
            log.error("JSqlParse工具解析sql中的Like字段异常", e);
        }
        return null;
    }

    public static void getLikeField(Expression expression, Set<String> likeFields) {
        if (expression == null) {
            return;
        }
        if (expression instanceof LikeExpression) {
            LikeExpression likeExpression = (LikeExpression) expression;
            if (likeExpression.getLeftExpression() instanceof Column) {
                likeFields.add(((Column) (likeExpression.getLeftExpression())).getColumnName());
                return;
            }
        }
        getLikeField(getLeftExpression(expression), likeFields);
        getLikeField(getRightExpression(expression), likeFields);
    }
}
