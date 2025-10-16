package com.example.kkproj.service;

import com.example.kkproj.config.redis.TokenStore;
import com.example.kkproj.model.UserVo;
import com.example.kkproj.properties.JwtProperties;
import com.example.kkproj.util.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.UUID;

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

  public void logout(String userId, String accessToken) {
    // AccessToken 를 블랙리스트에 추가
    Jws<Claims> jws = jws
  }

}
