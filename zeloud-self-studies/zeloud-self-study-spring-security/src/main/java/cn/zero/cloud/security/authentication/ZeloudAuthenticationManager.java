package cn.zero.cloud.security.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.List;

/**
 * 自定义 AuthenticationManager，根据用户登录时的 username 和 password，验证是否匹配 DB
 * 参考 {@link org.springframework.security.authentication.ProviderManager}
 *
 * @author Xisun Wang
 * @since 7/10/2024 10:09
 */
@Slf4j
public class ZeloudAuthenticationManager implements AuthenticationManager {
    private final List<AuthenticationProvider> providers;

    public ZeloudAuthenticationManager(List<AuthenticationProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication result = null;

        // 如果设置多个 AuthenticationProvider，根据实际业务需求确定 authentication 逻辑
        for (AuthenticationProvider provider : providers) {
            try {
                result = provider.authenticate(authentication);
                if (result != null) {
                    // 根据实际业务需求，确定此逻辑
                    copyDetails(authentication, result);
                    break;
                }
            } catch (Exception e) {
                log.error("Authentication failed for provider: {}", provider, e);
                throw e;
            }
        }

        return result;
    }

    private void copyDetails(Authentication source, Authentication dest) {
        if ((dest instanceof AbstractAuthenticationToken token) && (dest.getDetails() == null)) {
            token.setDetails(source.getDetails());
        }
    }
}
