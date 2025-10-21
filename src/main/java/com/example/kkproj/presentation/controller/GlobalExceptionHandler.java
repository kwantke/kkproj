package com.example.kkproj.presentation.controller;

import com.example.kkproj.domain.exception.NotFoundShortenUrlException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(NotFoundShortenUrlException.class)
  public ResponseEntity<String> handleNotFoundShortenUrlException(
          NotFoundShortenUrlException ex
  ) {
    return new ResponseEntity<>("단축 URL을 찾지 못했습니다.", HttpStatus.NOT_FOUND);
  }
}
