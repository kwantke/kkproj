package com.example.kkproj.presentation.controller;

import com.example.kkproj.application.SimpleShortenUrlService;
import com.example.kkproj.presentation.dto.ShortenUrlCreateRequestDto;
import com.example.kkproj.presentation.dto.ShortenUrlCreateResponseDto;
import com.example.kkproj.presentation.dto.ShortenUrlInformationDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
public class ShortenUrlRestController {

  private final SimpleShortenUrlService simpleShortenUrlService;

  @RequestMapping(value = "/shortenUrl", method = RequestMethod.POST)
  public ResponseEntity<ShortenUrlCreateResponseDto> createShortenUrl(
          @Valid @RequestBody ShortenUrlCreateRequestDto shortenUrlCreateRequestDto
  ) {
    ShortenUrlCreateResponseDto shortenUrlCreateResponseDto =
            simpleShortenUrlService.generateShortenUrl(shortenUrlCreateRequestDto);
    return ResponseEntity.ok(shortenUrlCreateResponseDto);
  }

  @RequestMapping(value = "{shortenUrlKey}", method = RequestMethod.GET)
  public ResponseEntity<?> redirectShortenUrl(
          @PathVariable String shortenUrlKey
  ) throws URISyntaxException {
    String originalUrl = simpleShortenUrlService.getOriginalUrlByShortenUrlKey(shortenUrlKey);
    URI redirectUri = new URI(originalUrl);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setLocation(redirectUri);
    return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
  }

  @RequestMapping(value = "/shortenUrl/{shortenUrlKey}", method = RequestMethod.GET)
  public ResponseEntity<ShortenUrlInformationDto> getShortenUrlInformation(
          @PathVariable String shortenUrlKey
  ) {
    ShortenUrlInformationDto shortenUrlInformationDto =
            simpleShortenUrlService.getShortenUrlInformationByShortenUrlKey(shortenUrlKey);
    return ResponseEntity.ok(shortenUrlInformationDto);
  }

}
