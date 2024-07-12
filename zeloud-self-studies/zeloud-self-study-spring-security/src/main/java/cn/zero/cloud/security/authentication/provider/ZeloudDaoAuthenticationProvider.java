package cn.zero.cloud.security.authentication.provider;

import cn.zero.cloud.security.pojo.auth.ZeloudAuthenticationToken;
import cn.zero.cloud.security.pojo.auth.ZeloudUserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 参考 {@link org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider}
 *
 * @author Xisun Wang
 * @since 7/10/2024 15:43
 */
@Slf4j
public class ZeloudDaoAuthenticationProvider implements AuthenticationProvider, InitializingBean, Ordered {
    private int order = -1;

    private boolean throwExceptionWhenTokenRejected;

    private final PasswordEncoder passwordEncoder;

    public ZeloudDaoAuthenticationProvider(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int i) {
        this.order = i;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }

        log.debug("PreAuthenticated authentication request: {}", authentication);
        if (authentication.getPrincipal() == null) {
            log.debug("No pre-authenticated principal found in request.");
            if (this.throwExceptionWhenTokenRejected) {
                throw new BadCredentialsException("No pre-authenticated principal found in request.");
            }
            return null;
        }

        if (authentication.getCredentials() == null) {
            log.debug("No pre-authenticated credentials found in request.");
            if (this.throwExceptionWhenTokenRejected) {
                throw new BadCredentialsException("No pre-authenticated credentials found in request.");
            }
            return null;
        }

        // username
        String principal = authentication.getPrincipal().toString();
        // password
        String credentials = authentication.getCredentials().toString();

        // 根据 username 查询 DB，然后验证 password 是否匹配，此处省略查询 DB 流程，并假设密码为 abcde
        if (!passwordEncoder.matches(credentials, passwordEncoder.encode("abcde"))) {
            log.debug("Failed authenticated as {}", principal);
            throw new BadCredentialsException("Bad details");
        }

        // 验证成功，返回一个完整的 Authentication
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // 用户的 role，应该从 DB 中获取，此处简化此过程
        authorities.add(() -> "USER_LIST");
        ZeloudUserPrincipal zeloudUserPrincipal = new ZeloudUserPrincipal("001", principal, "002");
        // 创建一个 ZeloudAuthenticationToken，保存用户信息，但不保存用户密码
        ZeloudAuthenticationToken zeloudAuthenticationToken = new ZeloudAuthenticationToken(zeloudUserPrincipal, null, authorities);

        // 将用户信息放入 SecurityContextHolder 中
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(zeloudAuthenticationToken);
        SecurityContextHolder.setContext(context);

        return zeloudAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public void setThrowExceptionWhenTokenRejected(boolean throwExceptionWhenTokenRejected) {
        this.throwExceptionWhenTokenRejected = throwExceptionWhenTokenRejected;
    }
}
