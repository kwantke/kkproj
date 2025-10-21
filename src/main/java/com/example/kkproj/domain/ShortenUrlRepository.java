package com.example.kkproj.domain;

import org.springframework.context.annotation.Configuration;


public interface ShortenUrlRepository {
  void saveShortenUrl(ShortenUrl shortenUrl);

  ShortenUrl findShortenUrlByShortenUrlKey(String shortenUrlKey);
}
