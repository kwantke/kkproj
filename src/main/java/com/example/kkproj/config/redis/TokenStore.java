package com.example.kkproj.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenStore {
  private final StringRedisTemplate redis;

  private static final String RT_KEY = "rt:%s"; // rt:{userId} -> refreshJti
  private static final String AT_BL = "bl:at:%s";

}
