package com.example.kkproj.util;


import com.example.kkproj.model.UserVo;
import com.example.kkproj.properties.JwtProperties;
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
  private static final String UF_ID       = "id";
  private static final String UF_NAME     = "username";
  private static final String UF_GENDER   = "gender";

  private Key key() {
    return Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
  }

  public String generateAccessToken(UserVo userVo) {

    Instant now = Instant.now();
    String jti = UUID.randomUUID().toString();

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

  public String generateRefreshToken(UserVo userVo){
    Instant now = Instant.now();


  }

}

