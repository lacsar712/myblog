package com.myblog.backend.common;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {
  private final String code;
  private final HttpStatus httpStatus;

  public AppException(String code, String message, HttpStatus httpStatus) {
    super(message);
    this.code = code;
    this.httpStatus = httpStatus;
  }

  public String code() {
    return code;
  }

  public HttpStatus httpStatus() {
    return httpStatus;
  }

  public static AppException notFound(String message) {
    return new AppException("NOT_FOUND", message, HttpStatus.NOT_FOUND);
  }

  public static AppException conflict(String message) {
    return new AppException("CONFLICT", message, HttpStatus.CONFLICT);
  }
}

