package cn.zero.cloud.security.pojo.auth;

import java.io.Serial;
import java.io.Serializable;

/**
 * 参考 {@link org.springframework.security.core.userdetails.User}
 *
 * @author Xisun Wang
 * @since 7/11/2024 09:46
 */
public class ZeloudUserPrincipal implements Serializable {
    @Serial
    private static final long serialVersionUID = 5063264531101070720L;

    private final String userId;

    private final String userName;

    private final String orgId;

    public ZeloudUserPrincipal(String userId, String userName, String orgId) {
        this.userId = userId;
        this.userName = userName;
        this.orgId = orgId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getOrgId() {
        return orgId;
    }
}
