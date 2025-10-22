package com.example.kkproj.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  NOT_FOUND_SHORTENURL(HttpStatus.NOT_FOUND,"단축 URL을 찾지 못했습니다."),
  LACK_OF_SHORTENURL_KEY(HttpStatus.INTERNAL_SERVER_ERROR,"단축 URL 자원이 부족합니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"내부 에러가 발생하였습니다."),
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"토큰이 올바르지 못합니다.")

  ;
  private HttpStatus status;
  private String message;
}
