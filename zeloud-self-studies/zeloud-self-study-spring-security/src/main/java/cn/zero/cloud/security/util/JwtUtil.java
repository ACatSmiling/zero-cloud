package cn.zero.cloud.security.util;

import cn.hutool.jwt.JWTUtil;
import cn.zero.cloud.security.pojo.auth.ZeloudUserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Xisun Wang
 * @since 7/10/2024 09:47
 */
@Component
public class JwtUtil {
    /**
     * client id
     */
    @Value("${jwt.client.id}")
    private String clientId;

    /**
     * client secret
     */
    @Value("${jwt.client.secret}")
    private String clientSecret;

    /**
     * 有效期，转换为天
     */
    @Value("${jwt.expiration}")
    private long expiration;

    public static final String JWT_USER_ID = "USER_ID";

    public static final String JWT_USER_NAME = "USER_NAME";

    public static final String JWT_USER_ROLE = "USER_ROLE";

    public static final String JWT_ORG_ID = "ORG_ID";

    public static final String JWT_EXPIRE_TIME = "EXPIRE_TIME";

    /**
     * @param principal   用户标识
     * @param authorities 用户权限
     * @return JWT
     */
    public String generateToken(ZeloudUserPrincipal principal, Collection<GrantedAuthority> authorities) {
        // 创建 Header
        Map<String, Object> headerClaims = new HashMap<>();
        headerClaims.put("alg", "HS256");
        headerClaims.put("typ", "JWT");

        // 创建 Payload
        Map<String, Object> payloadClaims = new HashMap<>();
        payloadClaims.put(JWT_USER_ID, principal.getUserId());
        payloadClaims.put(JWT_USER_NAME, principal.getUserName());
        List<String> roles = new ArrayList<>(authorities.size());
        for (GrantedAuthority authority : authorities) {
            roles.add(authority.getAuthority());
        }
        payloadClaims.put(JWT_USER_ROLE, roles);
        payloadClaims.put(JWT_ORG_ID, principal.getOrgId());
        payloadClaims.put(JWT_EXPIRE_TIME, System.currentTimeMillis() + 1000 * 60 * 60 * 24 * expiration);

        // 创建签名密钥
        String key = clientId + ":" + clientSecret;

        return JWTUtil.createToken(headerClaims, payloadClaims, key.getBytes());
    }
}