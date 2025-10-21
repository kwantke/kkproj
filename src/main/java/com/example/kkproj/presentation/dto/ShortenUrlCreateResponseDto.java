package com.example.kkproj.presentation.dto;

import com.example.kkproj.domain.ShortenUrl;
import lombok.Getter;

@Getter
public class ShortenUrlCreateResponseDto {

  private String originalUrl;
  private String shortenUrlKey;

  public ShortenUrlCreateResponseDto(ShortenUrl shortenUrl) {
    this.originalUrl = shortenUrl.getOriginalUrl();
    this.shortenUrlKey = shortenUrl.getShortenUrlKey();
  }
}
