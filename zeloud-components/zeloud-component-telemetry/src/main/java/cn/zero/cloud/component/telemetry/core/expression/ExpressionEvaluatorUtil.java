package cn.zero.cloud.component.telemetry.core.expression;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;

import java.lang.reflect.Method;

/**
 * @author Xisun Wang
 * @since 2024/3/21 10:12
 */
public class ExpressionEvaluatorUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExpressionEvaluatorUtil.class);

    private static final ExpressionEvaluator<String> EXPRESSION_EVALUATOR = new ExpressionEvaluator<>();

    private ExpressionEvaluatorUtil() {
        throw new IllegalStateException("Utility class!");
    }

    /**
     * Spring Expression Language，SqEL 表达式评估
     *
     * @param joinPoint          切面
     * @param resourceExpression 待评估的 SqEL
     * @return 评估结果
     */
    public static String getString(JoinPoint joinPoint, String resourceExpression) {
        if (joinPoint.getArgs() == null || StringUtils.isEmpty(resourceExpression)) {
            return null;
        }

        // 创建 SqEL 上下文
        EvaluationContext evaluationContext = EXPRESSION_EVALUATOR.createEvaluationContext(joinPoint.getTarget(), joinPoint.getTarget().getClass(),
                ((MethodSignature) joinPoint.getSignature()).getMethod(), joinPoint.getArgs());

        // 创建缓存的 key
        AnnotatedElementKey methodKey = new AnnotatedElementKey(((MethodSignature) joinPoint.getSignature()).getMethod(), joinPoint.getTarget().getClass());

        try {
            // 评估 SqEL，返回结果
            return EXPRESSION_EVALUATOR.condition(resourceExpression, methodKey, evaluationContext, String.class);
        } catch (Exception e) {
            // 评估异常，返回 null
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            LOGGER.warn("Expression evaluator flied, method: {}, resourceExpression: {}, exception message: {}", method.toString(), resourceExpression, e.getMessage());
            return null;
        }
    }
}
