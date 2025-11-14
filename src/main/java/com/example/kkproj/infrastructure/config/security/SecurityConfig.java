package com.example.kkproj.infrastructure.config.security;

import com.example.kkproj.infrastructure.config.filter.JwtAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable());

    http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/login", "/auth/refresh", "/auth/join", "/health").permitAll()
            .requestMatchers("/auth/logout").authenticated() // ✅ 로그아웃은 인증 필요
            .anyRequest().authenticated()
    );

    http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    http.exceptionHandling(e-> e
            .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
            .accessDeniedHandler((req, res, ex) -> res.sendError(HttpServletResponse.SC_FORBIDDEN))
    );

    return http.build();

  }

}
