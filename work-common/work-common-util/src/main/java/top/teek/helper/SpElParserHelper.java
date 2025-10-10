package top.teek.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2024/6/26 23:33:55
 * @note SpEl 解析工具类
 */
@Slf4j
public class SpElParserHelper {

    /**
     * 模板解析器
     */
    private static final TemplateParserContext TEMPLATE_PARSER_CONTEXT = new TemplateParserContext();

    /**
     * 表达式解析器
     */
    private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    /**
     * 参数名解析器，用于获取参数名
     */
    private static final DefaultParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * 解析 SpEL 表达式
     *
     * @param method         方法
     * @param args           参数值
     * @param spElExpression 表达式
     * @param clz            返回结果的类型
     * @return 执行 SpEL 表达式后的结果
     */
    public static <T> T parse(Method method, Object[] args, String spElExpression, Class<T> clz) {
        MethodBasedEvaluationContext context = new MethodBasedEvaluationContext("", method, args, PARAMETER_NAME_DISCOVERER);
        ListableBeanFactory beanFactory = SpringHelper.getBeanFactoryIfPresent();
        if (Objects.nonNull(beanFactory)) {
            // 设置 ApplicationContext 到 Context 中，使用 @ 符合可以使用容器的 Bean 方法，如 #@WorkController.login()
            context.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }

        return getResult(context, spElExpression, clz);
    }

    /**
     * 解析 SpEL 表达式
     *
     * @param method         方法
     * @param args           参数值
     * @param spElExpression 表达式
     * @param clz            返回结果的类型
     * @param defaultResult  默认结果
     * @return 执行 SpEL 表达式后的结果
     */
    public static <T> T parse(Method method, Object[] args, String spElExpression, Class<T> clz, T defaultResult) {
        MethodBasedEvaluationContext context = new MethodBasedEvaluationContext("", method, args, PARAMETER_NAME_DISCOVERER);
        ListableBeanFactory beanFactory = SpringHelper.getBeanFactoryIfPresent();
        if (Objects.nonNull(beanFactory)) {
            // 设置 ApplicationContext 到 Context 中，使用 @ 符合可以使用容器的 Bean 方法，如 #@WorkController.login()
            context.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }

        T result = getResult(context, spElExpression, clz);
        if (Objects.isNull(result)) {
            return defaultResult;
        }
        return result;
    }

    /**
     * 解析 SpEL 表达式
     *
     * @param paramName      参数名
     * @param paramValue     参数值
     * @param spElExpression 表达式
     * @param clz            返回结果的类型
     * @return 执行 SpEL 表达式后的结果
     */
    public static <T> T parse(String paramName, Object paramValue, String spElExpression, Class<T> clz) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        ListableBeanFactory beanFactory = SpringHelper.getBeanFactoryIfPresent();
        if (Objects.nonNull(beanFactory)) {
            // 设置 ApplicationContext 到 Context 中，使用 @ 符合可以使用容器的 Bean 方法，如 #@WorkController.login()
            context.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
        // 设置上下文变量
        context.setVariable(paramName, paramValue);
        return getResult(context, spElExpression, clz);
    }


    /**
     * 解析 SpEL 表达式
     *
     * @param paramName      参数名
     * @param paramValue     参数值
     * @param spElExpression 表达式
     * @param clz            返回结果的类型
     * @param defaultResult  默认结果
     * @return 执行 SpEL表达式后的结果
     */
    public static <T> T parse(String paramName, Object paramValue, String spElExpression, Class<T> clz, T defaultResult) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        ListableBeanFactory beanFactory = SpringHelper.getBeanFactoryIfPresent();
        if (Objects.nonNull(beanFactory)) {
            // 设置 ApplicationContext 到 Context 中，使用 @ 符合可以使用容器的 Bean 方法，如 #@WorkController.login()
            context.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
        // 设置上下文变量
        context.setVariable(paramName, paramValue);
        T result = getResult(context, spElExpression, clz);
        if (Objects.isNull(result)) {
            return defaultResult;
        }
        return result;

    }


    /**
     * 获取 SpEL 表达式后的结果
     *
     * @param context        解析器上下文接口
     * @param spElExpression 表达式
     * @param clz            返回结果的类型
     * @return 执行SpEL 表达式后的结果
     */
    private static <T> T getResult(EvaluationContext context, String spElExpression, Class<T> clz) {
        try {
            // 解析表达式
            Expression expression = parseExpression(spElExpression);
            // 获取表达式的值
            return expression.getValue(context, clz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * 解析表达式
     *
     * @param spElExpression spEl 表达式
     * @return 表达式
     */
    private static Expression parseExpression(String spElExpression) {
        // 如果表达式是一个 #{} 表达式
        if (StringUtils.startsWith(spElExpression, TEMPLATE_PARSER_CONTEXT.getExpressionPrefix()) && StringUtils.endsWith(spElExpression, TEMPLATE_PARSER_CONTEXT.getExpressionSuffix())) {
            return EXPRESSION_PARSER.parseExpression(spElExpression, TEMPLATE_PARSER_CONTEXT);
        }
        // 如果表达式是一个 # 表达式
        return EXPRESSION_PARSER.parseExpression(spElExpression);
    }
}
