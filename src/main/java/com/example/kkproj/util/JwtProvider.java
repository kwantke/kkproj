package com.example.kkproj.util;


import com.example.kkproj.model.UserVo;
import com.example.kkproj.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtProvider {

  private final JwtProperties props; // @ConfigurationProperties("jwt")

  // Claim Keys
  private static final String CLAIM_ROLES = "roles";
  private static final String CLAIM_USER = "usr";
  private static final String UF_ID       = "id"; //UF : User Form
  private static final String UF_NAME     = "username";
  private static final String UF_GENDER   = "gender";

  private Key key() {
    return Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
  }

  public String generateAccessToken(UserVo userVo) {

    Instant now = Instant.now();
    String jti = UUID.randomUUID().toString(); // jwt Token Id

    Map<String, Object> principal =new HashMap();
    principal.put(UF_ID, userVo.getUserId());
    principal.put(UF_NAME, userVo.getUsername());

    return Jwts.builder()
            .setId(jti)
            .setSubject(String.valueOf(userVo.getUserId()))
            .claim(CLAIM_USER,principal)
            .setIssuer(props.getIssuer())
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plus(props.getAccessExpMin(), ChronoUnit.MINUTES)))
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact();

  }

  public String generateRefreshToken(UserVo userVo, String refeshJti){
    Instant now = Instant.now();
    Map<String, Object> principal =new HashMap();
    principal.put(UF_ID, userVo.getUserId());
    principal.put(UF_NAME, userVo.getUsername());

    return Jwts.builder()
            .setId(refeshJti) // refresh jti
            .setSubject(String.valueOf(userVo.getUserId()))
            .claim(CLAIM_USER, principal)
            .setIssuer(props.getIssuer())
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plus(props.getRefreshExpDays(), ChronoUnit.DAYS)))
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact();

  }

  // token으로 claim 반환
  public Jws<Claims> parse(String token) {

    return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
  }

  public boolean isExpired(Claims c) {
    Date exp = c.getExpiration();
    return exp != null && exp.before(new Date());
  }

  // 토큰 고유 ID
  public String getJti(Claims claims) {
    return claims.getId();
  }

  // sub에서 userId 반환
  public String getUserId(Claims c) {

    String sub = c.getSubject();
    if(sub == null) return null;

    return sub;
  }








}

