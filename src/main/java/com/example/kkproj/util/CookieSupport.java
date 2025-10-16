package com.example.kkproj.util;

import com.example.kkproj.properties.JwtProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CookieSupport {
  private final JwtProperties props;

  public void writeRefreshCookie(HttpServletResponse res, String refreshToken, int maxAgeSec) {
    ResponseCookie cookie = ResponseCookie
            .from(props.getCookie().getRefreshName(), refreshToken)
            .httpOnly(props.getCookie().isHttpOnly())
            .secure(props.getCookie().isSecure())
            .domain(props.getCookie().getDomain())
            .sameSite(props.getCookie().getSameSite())
            .path("/")
            .maxAge(maxAgeSec)
            .build();
    res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }

  public Optional<String> readRefreshCookie(HttpServletRequest req) {
    if(req.getCookies() == null) return Optional.empty();
    for (Cookie c : req.getCookies()) {
      if (props.getCookie().getRefreshName().equals(c.getName())) {
        return Optional.ofNullable(c.getValue());
      }
    }
    return Optional.empty();
  }

  public void clearRefreshCookie(HttpServletResponse res) {
    ResponseCookie cookie = ResponseCookie.from(props.getCookie().getRefreshName(),"")
            .httpOnly(true)
            .secure(props.getCookie().isSecure())
            .domain(props.getCookie().getDomain())
            .sameSite(props.getCookie().getSameSite())
            .path("/")
            .maxAge(0)
            .build();
    res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }



}




