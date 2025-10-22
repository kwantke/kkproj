package com.example.kkproj.config.security;

import com.example.kkproj.domain.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

import static com.example.kkproj.domain.exception.ErrorCode.INVALID_TOKEN;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

    // 1️⃣ Authorization 헤더에서 토큰 추출
    String header = request.getHeader("Authorization");
    String token = null;
    if (header != null && header.startsWith("Bearer ")) {
      token = header.substring(7); // "Bearer " 이후의 실제 토큰 부분
    }

    // 2️⃣ 로그 출력
    log.warn("Unauthorized: {}, token={}", authException.getMessage(), token);
    log.warn("Unauthorized: {}", authException.toString()); // 또는 error
    String body = String.format("{\"error\":\"%s\",\"message\":\"%s\"}",
            INVALID_TOKEN.name(), INVALID_TOKEN.getMessage());

    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(INVALID_TOKEN.getStatus().value());
    response.getWriter().write(body);
  }
}
