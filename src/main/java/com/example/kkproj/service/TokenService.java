package com.example.kkproj.service;

import com.example.kkproj.config.redis.TokenStore;
import com.example.kkproj.model.UserVo;
import com.example.kkproj.properties.JwtProperties;
import com.example.kkproj.util.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.example.kkproj.util.JwtProvider.CLAIM_USER;

@Service
@RequiredArgsConstructor
public class TokenService {

  private final JwtProvider jwtProvider;
  private final TokenStore tokenStore;
  private final JwtProperties props;

  public record TokenPair(String accessToken, String refreshToken) {

  }

  public TokenPair issueTokens(UserVo userVo) {
    String at = jwtProvider.generateAccessToken(userVo);
    String rjti = UUID.randomUUID().toString();
    String rt = jwtProvider.generateRefreshToken(userVo, rjti);

    // Redis 저장 (TTL = refresh-exp)
    Duration rttl = Duration.ofDays(props.getRefreshExpDays());
    tokenStore.saveRefreshJti(userVo.getUserId(),rjti, rttl);

    return new TokenPair(at, rt);
  }

  public TokenPair rotateRefresh(String refreshToken) {
    Jws<Claims> jws = jwtProvider.parse(refreshToken);
    Claims c = jws.getBody();

    if (jwtProvider.isExpired(c)) {
      throw new JwtException("Refresh expired");
    }

    String userId = jwtProvider.getUserId(c);

    //발급된 토큰
    String presentedJti = jwtProvider.getJti(c);

    //Redis의 현재 jti 확인
    String currentJit = tokenStore.getRefreshJti(userId)
            .orElseThrow(() -> new JwtException("No refresh session"));

    // 발급된 토큰과 redis에 등록되어있는 토큰 일치 여부 확인
    if (!currentJit.equals(presentedJti)) {
      tokenStore.deleteRefreshJti(userId);
      throw new JwtException("Refresh resue detected");
    }

    Duration rttl = Duration.ofDays(props.getRefreshExpDays());
    tokenStore.markRefreshJtiUsed(presentedJti, rttl);

    UserVo userVo = (UserVo) c.get(CLAIM_USER);
    // 새 토근 발급
    String newAccess = jwtProvider.generateAccessToken(userVo);
    String newRjti = UUID.randomUUID().toString();
    String newRefresh = jwtProvider.generateRefreshToken(userVo, newRjti);

    // Redis jti 갱신
    tokenStore.saveRefreshJti(userId, newRjti, rttl);

    return new TokenPair(newAccess, newRefresh);
  }

  public void logout(UserVo userVo, String accessToken) {
    // AccessToken 를 블랙리스트에 추가
    Jws<Claims> jws = jwtProvider.parse(accessToken);
    Claims c = jws.getBody();
    String atJti = jwtProvider.getJti(c);

    Duration ttl = Duration.between(Instant.now(), c.getExpiration().toInstant());
    if (!ttl.isNegative()) {
      tokenStore.blacklistAccess(atJti, ttl);
    }

    // Refresh token 제거
    tokenStore.deleteRefreshJti(userVo.getUserId());
  }

}
