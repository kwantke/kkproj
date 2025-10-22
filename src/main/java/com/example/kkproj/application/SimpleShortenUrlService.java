package com.example.kkproj.application;

import com.example.kkproj.domain.exception.ErrorCode;
import com.example.kkproj.domain.exception.LackOfShortenUrlKeyException;
import com.example.kkproj.domain.exception.NotFoundShortenUrlException;
import com.example.kkproj.domain.ShortenUrl;
import com.example.kkproj.domain.ShortenUrlRepository;
import com.example.kkproj.presentation.dto.ShortenUrlCreateRequestDto;
import com.example.kkproj.presentation.dto.ShortenUrlCreateResponseDto;
import com.example.kkproj.presentation.dto.ShortenUrlInformationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleShortenUrlService {

  private final ShortenUrlRepository shortenUrlRepository;
  public ShortenUrlCreateResponseDto generateShortenUrl(
          ShortenUrlCreateRequestDto shortenUrlCreateRequestDto
  ) {
    String originalUrl = shortenUrlCreateRequestDto.getOriginalUrl();

    String shortenUrlKey = getUniqueShortenUrlKey();

    ShortenUrl shortenUrl = new ShortenUrl(originalUrl, shortenUrlKey);
    shortenUrlRepository.saveShortenUrl(shortenUrl);
    log.info("shortenUrl 생성: {}", shortenUrl);

    ShortenUrlCreateResponseDto shortenUrlCreateResponseDto
            = new ShortenUrlCreateResponseDto(shortenUrl);


    return shortenUrlCreateResponseDto;
  }

  public ShortenUrlInformationDto getShortenUrlInformationByShortenUrlKey(String shortenUrlKey) {
    ShortenUrl shortenUrl = shortenUrlRepository.findShortenUrlByShortenUrlKey(shortenUrlKey);

    if (null == shortenUrl) {
      throw new NotFoundShortenUrlException(ErrorCode.NOT_FOUND_SHORTENURL,String.format("shortenUrlKey= %s",shortenUrlKey));
    }
    ShortenUrlInformationDto shortenUrlInformationDto = new ShortenUrlInformationDto(shortenUrl);
    return shortenUrlInformationDto;
  }

  public String getOriginalUrlByShortenUrlKey(String shortenUrlKey) {
    ShortenUrl shortenUrl = shortenUrlRepository.findShortenUrlByShortenUrlKey(shortenUrlKey);

    if (null == shortenUrl) {
      throw new NotFoundShortenUrlException(ErrorCode.NOT_FOUND_SHORTENURL);
    }
    shortenUrl.increaseRedirectCount();
    shortenUrlRepository.saveShortenUrl(shortenUrl);

    String originalUrl = shortenUrl.getOriginalUrl();

    return originalUrl;
  }

  private String getUniqueShortenUrlKey() {
    final int MAX_RETRY_COUNT = 5;
    int count = 0;
    while (count++ < MAX_RETRY_COUNT) {
      String shortenUrlKey = ShortenUrl.generateShortenUrlKey();
      ShortenUrl shortenUrl
              = shortenUrlRepository.findShortenUrlByShortenUrlKey(shortenUrlKey);

      if(null == shortenUrl) return shortenUrlKey;
    }

    throw new LackOfShortenUrlKeyException();
  }
}
