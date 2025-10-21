package com.example.kkproj.domain;

import lombok.Getter;

@Getter
public class ShortenUrl {
  private String originalUrl;
  private String shortenUrlKey;
  private Long redirectCount;
}
