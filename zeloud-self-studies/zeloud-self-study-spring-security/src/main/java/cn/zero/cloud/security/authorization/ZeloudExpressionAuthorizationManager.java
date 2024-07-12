package cn.zero.cloud.security.authorization;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

/**
 * 自定义 AuthorizationManager，WebExpressionAuthorizationManager 可支持 SpEL 表达式
 * 参考 {@link org.springframework.security.web.access.expression.WebExpressionAuthorizationManager}
 *
 * @author Xisun Wang
 * @since 7/10/2024 17:26
 */
public class ZeloudExpressionAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        // TODO
        return null;
    }
}
