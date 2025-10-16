package com.example.kkproj.config.filter;

import com.example.kkproj.config.redis.TokenStore;
import com.example.kkproj.util.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;
  private final TokenStore tokenStore;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();

    return path.startsWith("/auth/") || path.startsWith("health");

  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (header == null || !header.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    final String token = header.substring(7);;

    Jws<Claims> jws = jwtProvider.parse(token);
    Claims c = jws.getBody();

    // 토근 만료 여부 확인
    if (jwtProvider.isExpired(c)) {
      throw new ExpiredJwtException(jws.getHeader(), c, "Access toekn expired");
    }
    String accessJti = jwtProvider.getJti(c);

    // 토큰 블랙리스트 확인
    if (tokenStore.isAccessBlacklisted(accessJti)) {
      throw new JwtException("Access token blacklisted");
    }


    String userId = jwtProvider.getUserId(c);




  }
}
