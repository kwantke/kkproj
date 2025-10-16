package com.example.kkproj.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
  private String issuer;
  private int accessExpMin;
  private int refreshExpDays;
  private String secret;

  private CookieProps cookie = new CookieProps();

  // 클래스 이름이 'cookie'라는 명칭만 있으면 cookie.* 을 읽어드린다
  // CookieSetting, JwtCookieSettings 이런식으로해도 된다
  @Data
  public static class CookieProps {
    private String refreshName;
    private String domain;
    private boolean secure;
    private boolean httpOnly;
    private String sameSite;
  }

}
