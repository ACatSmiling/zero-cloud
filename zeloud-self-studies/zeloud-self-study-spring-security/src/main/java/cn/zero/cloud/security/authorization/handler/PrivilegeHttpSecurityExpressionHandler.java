package cn.zero.cloud.security.authorization.handler;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.expression.DefaultHttpSecurityExpressionHandler;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author Xisun Wang
 * @since 7/11/2024 10:03
 */
public class PrivilegeHttpSecurityExpressionHandler extends DefaultHttpSecurityExpressionHandler {
    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    public PrivilegeHttpSecurityExpressionHandler() {
    }

    public EvaluationContext createEvaluationContext(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        SecurityExpressionOperations root = this.createSecurityExpressionRoot(authentication.get(), context);
        StandardEvaluationContext ctx = new StandardEvaluationContext(root);
        ctx.setBeanResolver(this.getBeanResolver());
        Map<String, String> var10000 = context.getVariables();
        Objects.requireNonNull(ctx);
        var10000.forEach(ctx::setVariable);
        return ctx;
    }

    protected SecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, RequestAuthorizationContext context) {
        PrivilegeHttpSecurityExpressionRoot root = new PrivilegeHttpSecurityExpressionRoot(authentication, context);
        root.setPermissionEvaluator(this.getPermissionEvaluator());
        root.setTrustResolver(this.trustResolver);
        root.setRoleHierarchy(this.getRoleHierarchy());
        String defaultRolePrefix = "ROLE_";
        root.setDefaultRolePrefix(defaultRolePrefix);
        return root;
    }


}
