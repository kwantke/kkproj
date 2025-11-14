package com.example.kkproj.infrastructure.config.filter;

import com.example.kkproj.presentation.dto.CachedBodyHttpServletRequest;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class LoggingFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    if (request instanceof HttpServletRequest httpServletRequest && response instanceof HttpServletResponse httpResponse) {
      // 요청을 CachedBodyHttpServletRequest로 래핑
      CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(httpServletRequest);
      ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(httpResponse);


      // URL, 메서드 및 요청 바디 로깅
      String url = wrappedRequest.getRequestURI();
      String method = wrappedRequest.getMethod();
      String body = wrappedRequest.getReader().lines().reduce("", String::concat);

      log.info("Incoming Request: URL={}, Method={}, Body={}", url, method, body);

      // 래핑된 요청 객체를 다음 필터 체인으로 전달
      chain.doFilter(wrappedRequest, wrappedResponse);

      // 응답 본문 로깅
      String responseBody = new String(wrappedResponse.getContentAsByteArray(), StandardCharsets.UTF_8);
      log.info("Outgoing Response: Status={}, Body={}",
              wrappedResponse.getStatus(), responseBody);

      // 중요: 실제 응답을 클라이언트로 전달 (버퍼 복사)
      wrappedResponse.copyBodyToResponse();

    } else {
      // HttpServletRequest가 아닌 경우 그대로 전달
      chain.doFilter(request, response);
    }
  }
}