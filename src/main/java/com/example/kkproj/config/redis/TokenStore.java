package com.example.kkproj.config.redis;

import com.example.kkproj.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenStore {
  private final StringRedisTemplate redis;

  private static final String RT_KEY = "rt:%s";
  private static final String RJ_KEY = "rj:%s";
  private static final String BL_KEY = "bl:at:%s";


  private final JwtProperties props;

  public void saveRefreshJti(String userId, String refreshJti, Duration ttl) {
    String key = RT_KEY.formatted(userId);
    redis.opsForValue().set(key, refreshJti, ttl);
  }

  public Optional<String> getRefreshJti(String userId) {
    String val = redis.opsForValue().get(RT_KEY.formatted(userId));
    return Optional.ofNullable(val);
  }

  // 사용된 refresh 토큰은 redis에 별도 등록하여 관리
  public void markRefreshJtiUsed(String jti, Duration ttl) {
    redis.opsForValue().set(RJ_KEY.formatted(jti),"used",ttl);
  }

  public boolean isRefreshJtiUsed(String jti) {
    return redis.hasKey(RJ_KEY.formatted(jti)) == Boolean.TRUE;
  }

  public void deleteRefreshJti(String userId) {
    redis.delete(RT_KEY.formatted(userId));
  }

  // 로그 아웃시 accessToken을 블랙리스트 토근으로 저장
  // ttl은 남은 잔여 시간으로 적용
  public void blacklistAccess(String accessJti, Duration ttl) {
    redis.opsForValue().set(BL_KEY.formatted(accessJti),"1",ttl);
  }

  public boolean isAccessBlacklisted(String accessJti) {
    return redis.hasKey(BL_KEY.formatted(accessJti)) == Boolean.TRUE;
  }


}
