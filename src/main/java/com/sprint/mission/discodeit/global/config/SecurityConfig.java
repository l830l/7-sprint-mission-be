package com.sprint.mission.discodeit.global.config;

import com.sprint.mission.discodeit.global.security.csrf.SpaCsrfTokenRequestHandler;
import com.sprint.mission.discodeit.global.security.handler.DiscodeitAccessDeniedHandler;
import com.sprint.mission.discodeit.global.security.handler.LoginFailureHandler;
import com.sprint.mission.discodeit.global.security.handler.LoginSuccessHandler;
import com.sprint.mission.discodeit.global.security.user.detail.DiscodeitUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final DiscodeitAccessDeniedHandler accessDeniedHandler;
    private final SessionRegistry sessionRegistry;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, DiscodeitUserDetailsService discodeitUserDetailsService) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/email-code",
                                "/api/auth/email-code/verify",
                                "/api/auth/csrf-token",
                                "/api-docs-ui.html",
                                "/api-docs-ui/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/my-api/**",
                                "/actuator/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/duplication/email").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/duplication/nickname").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/id").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/password/reset").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/binary-contents/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/auth/role").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(
                        exception -> exception.accessDeniedHandler(
                                accessDeniedHandler
                        )
                )
                .formLogin(login -> login
                        .loginProcessingUrl("/api/auth/login")
                        .usernameParameter("nickname")
                        .passwordParameter("password")
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailureHandler)
                )
                .sessionManagement(management -> management
                        .sessionConcurrency(concurrency -> concurrency
                                .maximumSessions(1)
                                .maxSessionsPreventsLogin(false)
                                .sessionRegistry(sessionRegistry))
                )
                .rememberMe(remember -> remember
                        .rememberMeParameter("rememberMe")
                        .userDetailsService(discodeitUserDetailsService)
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
                )
                .build();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        String roleHierarchy = String.join(
                "\n",
                "ROLE_ADMIN > ROLE_CHANNEL_MANAGER",
                "ROLE_CHANNEL_MANAGER > ROLE_USER"
        );
        return RoleHierarchyImpl.fromHierarchy(roleHierarchy);
    }

    @Bean
    public static MethodSecurityExpressionHandler methodSecurityExpressionHandler(
            RoleHierarchy roleHierarchy
    ) {
        DefaultMethodSecurityExpressionHandler handler =
                new DefaultMethodSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);
        return handler;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
