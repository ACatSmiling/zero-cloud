package cn.zero.cloud.security.pojo.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Xisun Wang
 * @since 7/12/2024 13:39
 */
public class ZeloudAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public ZeloudAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public ZeloudAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
