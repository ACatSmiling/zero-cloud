package cn.zero.cloud.security.config;

import cn.zero.cloud.security.authentication.ZeloudAuthenticationManager;
import cn.zero.cloud.security.authentication.provider.ZeloudDaoAuthenticationProvider;
import cn.zero.cloud.security.authorization.handler.PrivilegeHttpSecurityExpressionHandler;
import cn.zero.cloud.security.filter.JwtTokenAuthenticationFilter;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
 * @author Xisun Wang
 * @since 7/4/2024 15:29
 */
@Configuration
@EnableWebSecurity // 启用 Spring Security 的 Web 安全配置，Spring Boot 项目可以省略，默认在 AutoConfiguration 时已添加
public class WebSecurityConfig {
    @Value("${zeloud.security.is-debug}")
    private boolean isDebug;

    public static final List<String> IGNORED_URLS = List.of(
            "/css/**", "/html/**", "/img/**",
            "/js/**", "/libs/**", "/version.txt"
    );

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        // return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new BCryptPasswordEncoder();
    }

    /**
     * 基于内存的身份认证 Authentication，仅仅作为 example
     *
     * @return UserDetailsService
     */
    // @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // 创建基于内存的用户信息管理器
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        // 使用 manager 管理 UserDetails
        manager.createUser(
                // 创建 UserDetails，用于管理用户名称、用户密码、用户角色、用户权限等，此账号密码，会替换掉配置文件中默认的用户名称和密码
                // withDefaultPasswordEncoder() 已 Deprecated，新方法要注入一个 PasswordEncoder
                // User.withDefaultPasswordEncoder().username("user").password("abcde").roles("USER").build()
                User.withUsername("user").password(passwordEncoder.encode("abcde")).authorities("USER").build()
        );

        return manager;
    }

    @Bean
    public WebSecurityCustomizer configure() {
        // Define public access resources white list
        return web -> {
            web.ignoring().requestMatchers(IGNORED_URLS.stream().map(AntPathRequestMatcher::antMatcher).toList().toArray(new AntPathRequestMatcher[0]));
            if (isDebug) {
                web.ignoring().requestMatchers("/actuator/**") // For run monitor
                        .requestMatchers(antMatcher("/swagger-ui.html")) // For doc
                        .requestMatchers(antMatcher("/webjars/springfox-swagger-ui/**"))
                        .requestMatchers(antMatcher("/swagger-resources/**"))
                        .requestMatchers(antMatcher("/v2/api-docs/**"));
            }
        };
    }

    @Bean
    public JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter() {
        return new JwtTokenAuthenticationFilter();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        return new ZeloudDaoAuthenticationProvider(passwordEncoder);
    }

    @Bean
    public AuthenticationManager authenticationManager(List<AuthenticationProvider> authenticationProviders) {
        return new ZeloudAuthenticationManager(authenticationProviders);
    }

    @Bean
    public SecurityExpressionHandler<RequestAuthorizationContext> httpExpressionHandler() {
        return new PrivilegeHttpSecurityExpressionHandler();
    }

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> authorizationManager() {
        WebExpressionAuthorizationManager authorizationManager = new WebExpressionAuthorizationManager("isLoggedIn()");
        authorizationManager.setExpressionHandler(httpExpressionHandler());
        return authorizationManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter,
                                                   AuthenticationManager authenticationManager,
                                                   AuthorizationManager<RequestAuthorizationContext> authorizationManager) throws Exception {
        http
                .authenticationManager(authenticationManager)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(antMatcher("/**")).access(authorizationManager));

        /*http.authorizeRequests(
                authorize -> authorize
                        //具有USER_LIST权限的用户可以访问/user/list
                        .requestMatchers("/user/list").hasAuthority("USER_LIST")
                        //具有USER_ADD权限的用户可以访问/user/add
                        .requestMatchers("/user/add").hasAuthority("USER_ADD")
                        //对所有请求开启授权保护
                        .anyRequest()
                        //已认证的请求会被自动授权
                        .authenticated()
        );

        http
                .authorizeRequests((authorizeRequests) ->
                        authorizeRequests.anyRequest().hasRole("USER")
                )
                .formLogin((formLogin) ->
                        formLogin
                                .permitAll()
                )

                .sessionManagement((sessionManagement) -> sessionManagement
                        .invalidSessionUrl("/login?invalid-session=true") // &#x8BBE;&#x7F6E;&#x4F1A;&#x8BDD;&#x5931;&#x6548;&#x540E;&#x7684;&#x8DF3;&#x8F6C;URL
                        .sessionConcurrency((sessionConcurrency) -> sessionConcurrency
                                .maximumSessions(1) // &#x6307;&#x5B9A;&#x540C;&#x4E00;&#x8D26;&#x53F7;&#x7684;&#x6700;&#x5927;&#x5E76;&#x53D1;&#x767B;&#x5F55;&#x6570;&#x91CF;
                                .maxSessionsPreventsLogin(true) // &#x5F53;&#x8FBE;&#x5230;&#x6700;&#x5927;&#x4F1A;&#x8BDD;&#x6570;&#x65F6;&#x963B;&#x6B62;&#x65B0;&#x4F1A;&#x8BDD;&#x767B;&#x5F55;
                                .expiredUrl("/ login?expired") // &#x8BBE;&#x7F6E;&#x4F1A;&#x8BDD;&#x8FC7;&#x671F;&#x540E;&#x7684;&#x8DF3;&#x8F6C;URL
                        )
                );*/
        return http.build();
    }
}
