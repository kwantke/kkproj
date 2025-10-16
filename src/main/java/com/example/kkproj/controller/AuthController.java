package com.example.kkproj.controller;

import com.example.kkproj.dto.LoginRequest;
import com.example.kkproj.model.UserVo;
import com.example.kkproj.properties.JwtProperties;
import com.example.kkproj.service.TokenService;
import com.example.kkproj.service.UserService;
import com.example.kkproj.util.CookieSupport;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final CookieSupport cookieSupport;
  private final JwtProperties props;
  private final TokenService tokenService;
  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest req, HttpServletResponse res) throws Exception {
    // 실제 인증 로직
    UserVo userVo = userService.login(req);
    TokenService.TokenPair pair = tokenService.issueTokens(userVo);

    Authentication auth;
    // refreshToken -> HttpOnly 쿠키에 추가
    cookieSupport.writeRefreshCookie(res, pair.refreshToken(), (int) Duration.ofDays(props.getRefreshExpDays()).getSeconds());

    return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + pair.accessToken())
            .body(Map.of("accessToken", pair.accessToken()));
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refresh(HttpServletRequest req, HttpServletResponse res) {
    String refresh = cookieSupport.readRefreshCookie(req)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No refresh cookie"));

    TokenService.TokenPair pair = tokenService.rotateRefresh(refresh);

    cookieSupport.writeRefreshCookie(
            res, pair.refreshToken(), (int) Duration.ofDays(props.getRefreshExpDays()).getSeconds()
    );

    return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + pair.accessToken())
            .body(Map.of("accessToken", pair.accessToken()));
  }

  @PostMapping("logout")
  public ResponseEntity<?> logout(HttpServletRequest req, HttpServletResponse res, Authentication authentication) {
    if (authentication instanceof UsernamePasswordAuthenticationToken token
            && authentication.getPrincipal() instanceof UserVo userVo) {
      String header = req.getHeader(HttpHeaders.AUTHORIZATION);
      if (header != null && header.startsWith("Bearer ")) {
        tokenService.logout(userVo,header.substring(7));
      }
    }
    cookieSupport.clearRefreshCookie(res);

    return ResponseEntity.ok().build();
  }
}
