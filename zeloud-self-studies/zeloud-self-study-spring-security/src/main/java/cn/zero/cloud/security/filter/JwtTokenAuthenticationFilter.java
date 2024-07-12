package cn.zero.cloud.security.filter;

import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.zero.cloud.component.general.tool.utils.ZeloudJsonUtil;
import cn.zero.cloud.security.pojo.auth.ZeloudUserPrincipal;
import cn.zero.cloud.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.*;

/**
 * @author Xisun Wang
 * @since 7/12/2024 11:07
 */
public class JwtTokenAuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        this.logger.debug("Checking secure context token: " + SecurityContextHolder.getContext().getAuthentication());

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // 对登录请求放行，其他请求鉴定 jwtToken
        if (!isLoginRequest(request)) {
            String jwtToken = getJwtToken(request);
            authenticate(jwtToken);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isLoginRequest(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/zeloud/security/auth");
    }

    private String getJwtToken(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return request.getHeader("Authorization");
    }

    private void authenticate(String jwtToken) {
        if (StringUtils.isBlank(jwtToken)) {
            return;
        }

        // 解析 jwtToken
        JWT jwt = JWTUtil.parseToken(jwtToken);
        JSONObject headers = jwt.getHeaders();
        JSONObject payloads = jwt.getPayloads();

        // 解析 jwtToken 中的用户信息
        String userId = jwt.getPayload(JwtUtil.JWT_USER_ID).toString();
        String userName = jwt.getPayload(JwtUtil.JWT_USER_NAME).toString();
        String orgId = jwt.getPayload(JwtUtil.JWT_ORG_ID).toString();
        String userRole = jwt.getPayload(JwtUtil.JWT_USER_ROLE).toString();
        List roles = ZeloudJsonUtil.deserializeToClassType(userRole, List.class);

        // 解析 jwtToken 中的用户权限


        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (Object o : roles) {
            String role = (String) o;
            authorities.add(new SimpleGrantedAuthority(role));
        }
        ZeloudUserPrincipal zeloudUserPrincipal = new ZeloudUserPrincipal(userId, userName, orgId);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(zeloudUserPrincipal, null, authorities);

        // 将用户信息放入 SecurityContextHolder 中
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(usernamePasswordAuthenticationToken);
        SecurityContextHolder.setContext(context);
    }
}
