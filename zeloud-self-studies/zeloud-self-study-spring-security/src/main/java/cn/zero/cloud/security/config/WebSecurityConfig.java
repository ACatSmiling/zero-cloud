package cn.zero.cloud.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author Xisun Wang
 * @since 7/4/2024 15:29
 */
@Configuration
@EnableWebSecurity // 启用 Spring Security 的 Web 安全配置，Spring Boot 项目可以省略，默认已添加
public class WebSecurityConfig {
    @Bean
    public UserDetailsService userDetailsService() {
        // 创建基于内存的用户信息管理器
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        // 使用 manager 管理 UserDetails
        manager.createUser(
                // 创建 UserDetails，用于管理用户名称、用户密码、用户角色、用户权限等
                // 此账号密码，会替换掉配置文件中默认的用户名称和密码
                User.withDefaultPasswordEncoder().username("user").password("abcde").roles("USER").build()
        );

        return manager;
    }
}
