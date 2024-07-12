package cn.zero.cloud.security.authorization.handler;

import cn.zero.cloud.security.util.AuthenticationUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

/**
 * @author Xisun Wang
 * @since 7/11/2024 09:41
 */
public class PrivilegeHttpSecurityExpressionRoot extends SecurityExpressionRoot {
    public final HttpServletRequest request;

    public PrivilegeHttpSecurityExpressionRoot(Authentication authentication, RequestAuthorizationContext context) {
        super(authentication);
        this.request = context.getRequest();
    }

    /**
     * 定义 SpEL 表达式
     *
     * @return 是否登录
     */
    public boolean isLoggedIn() {
        return AuthenticationUtil.isUserLogin();
    }

    public boolean hasIpAddress(String ipAddress) {
        return (new IpAddressMatcher(ipAddress)).matches(this.request);
    }
}
