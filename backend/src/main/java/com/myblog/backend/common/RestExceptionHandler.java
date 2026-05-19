package com.myblog.backend.common;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

  @ExceptionHandler(AppException.class)
  public ResponseEntity<ApiResponse<Void>> handleAppException(AppException ex) {
    return ResponseEntity.status(ex.httpStatus()).body(ApiResponse.fail(ex.code(), ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
    var msg = ex.getBindingResult().getFieldErrors().stream().findFirst()
      .map(err -> err.getField() + ": " + err.getDefaultMessage())
      .orElse("参数校验失败");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail("BAD_REQUEST", msg));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail("BAD_REQUEST", ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleUnknown(Exception ex) {
    log.error("Unhandled exception", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(ApiResponse.fail("INTERNAL_ERROR", "服务异常"));
  }
}
