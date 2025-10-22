package com.example.kkproj.domain.exception;


public class NotFoundShortenUrlException extends ShortenKeyUrlException{

  public NotFoundShortenUrlException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public NotFoundShortenUrlException(ErrorCode errorCode) {
    super(errorCode);
  }
}
