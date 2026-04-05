package com.lincoln.kilimaniThrive.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll() // Allow viewing uploaded files
                        .requestMatchers(org.springframework.http.HttpMethod.GET,
                                "/api/v1/blog/**",
                                "/api/v1/bible-study/**",
                                "/api/v1/tag/**",
                                "/api/v1/comment/**"
                        ).permitAll()
                        // Publicly accessible POST endpoints
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/prayer-requests").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/prayer-requests/*/like").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/blog/*/like").permitAll()
                        // Require authentication for other data modifications and media uploads
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/**").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/v1/**").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/v1/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new org.springframework.security.web.authentication.HttpStatusEntryPoint(org.springframework.http.HttpStatus.UNAUTHORIZED))
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
