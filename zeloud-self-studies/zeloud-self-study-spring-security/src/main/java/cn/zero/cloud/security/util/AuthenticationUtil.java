package cn.zero.cloud.security.util;

import cn.zero.cloud.security.pojo.auth.ZeloudUserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * @author Xisun Wang
 * @since 7/11/2024 09:42
 */
public class AuthenticationUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationUtil.class);

    public AuthenticationUtil() {
    }

    public static boolean isUserLogin() {
        Optional<String> userIdOptional = getUserId();
        Optional<String> userNameOptional = getUserName();
        return userIdOptional.isPresent() || userNameOptional.isPresent();
    }

    public static Optional<String> getUserId() {
        ZeloudUserPrincipal principal = getSessionTicketPrincipal();
        return principal != null && principal.getUserId() != null ? Optional.of(principal.getUserId()) : Optional.empty();
    }

    public static Optional<String> getUserName() {
        ZeloudUserPrincipal principal = getSessionTicketPrincipal();
        return principal != null && principal.getUserName() != null ? Optional.of(principal.getUserName()) : Optional.empty();
    }

    private static ZeloudUserPrincipal getSessionTicketPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        } else {
            // 获取 Authentication 中保存的用户标识
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof ZeloudUserPrincipal)) {
                LOGGER.debug("Principal is not ZeloudUserPrincipal");
                return null;
            } else {
                return (ZeloudUserPrincipal) principal;
            }
        }
    }
}
