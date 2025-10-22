package com.example.kkproj.presentation.controller;

import com.example.kkproj.domain.exception.ErrorCode;
import com.example.kkproj.domain.exception.LackOfShortenUrlKeyException;
import com.example.kkproj.domain.exception.NotFoundShortenUrlException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(LackOfShortenUrlKeyException.class)
  public ResponseEntity<String> handleLackOfShortenUrlKeyException(
          LackOfShortenUrlKeyException ex
  ) {
    // 개발자에게 알려줄 수 있는 수단 필요
    log.error("단축 URL 자원이 부족합니다.");
    return new ResponseEntity<>("단축 URL 자원이 부족합니다.", HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(NotFoundShortenUrlException.class)
  public ResponseEntity<String> handleNotFoundShortenUrlException(
          NotFoundShortenUrlException ex
  ) {
    log.info(ex.getMessage());
    return new ResponseEntity<>(ErrorCode.NOT_FOUND_SHORTENURL.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleMethodArgumentNotValidException(
          MethodArgumentNotValidException ex
  ) {
    // 유효성 검증 오류 세부 정보 추출
    StringBuilder errorMessage = new StringBuilder("유효성 검증 실패: ");
    ex.getBindingResult().getFieldErrors().forEach(error -> {
      errorMessage.append(String.format("필드 '%s': %s. ", error.getField(), error.getDefaultMessage()));
    });

    // 상세 로그
    log.debug("잘못된 요청: {}", errorMessage);

    // 클라이언트에 응답
    return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<?> applicationHandler(RuntimeException e) {
    log.error("Error occurs {}", e.toString());

    return new ResponseEntity<>(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), HttpStatus.NOT_FOUND);
  }
}
