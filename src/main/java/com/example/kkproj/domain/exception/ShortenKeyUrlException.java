package com.example.kkproj.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShortenKeyUrlException extends RuntimeException{
  private ErrorCode errorCode;
  private String message;

  public ShortenKeyUrlException(ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.message = null;

  }

  @Override
  public String getMessage() {
    if (message == null) {
      return errorCode.getMessage();
    }
    return String.format("%s. %s", errorCode.getMessage(), message);
  }
}
