package com.example.kkproj.presentation.dto;

import com.example.kkproj.domain.ShortenUrl;
import lombok.Getter;

@Getter
public class ShortenUrlInformationDto {
  private String originalUrl;
  private String shortenUrlKey;
  private Long redirectCount;

  public ShortenUrlInformationDto(ShortenUrl shortenUrl) {
    this.originalUrl = shortenUrl.getOriginalUrl();
    this.shortenUrlKey = shortenUrl.getShortenUrlKey();
    this.redirectCount = shortenUrl.getRedirectCount();
  }
}
