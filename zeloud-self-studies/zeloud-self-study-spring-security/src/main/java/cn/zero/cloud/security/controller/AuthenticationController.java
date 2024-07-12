package cn.zero.cloud.security.controller;

import cn.zero.cloud.security.pojo.auth.ZeloudUserPrincipal;
import cn.zero.cloud.security.pojo.dto.UserDTO;
import cn.zero.cloud.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * @author Xisun Wang
 * @since 7/9/2024 16:42
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String login(@RequestBody UserDTO userDTO) {
        // 身份验证，验证成功后，会返回一个 Authentication，此处是 UsernamePasswordAuthenticationToken
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getUsername(),
                        userDTO.getPassword()
                )
        );

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;

        // 身份验证成功，获取 AuthenticationToken
        //  UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        // 用户标识，身份验证成功后，principal 设置的是 ZeloudUserPrincipal 实例
        ZeloudUserPrincipal principal = (ZeloudUserPrincipal) authentication.getPrincipal();
        // 权限列表
        Collection<GrantedAuthority> authorities = usernamePasswordAuthenticationToken.getAuthorities();

        // 根据用户信息，创建 JWT 并返回
        return jwtUtil.generateToken(principal, authorities);
    }
}
