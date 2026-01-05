package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.user.EmailInvalidException;
import com.sprint.mission.discodeit.exception.user.NicknameInvalidException;
import com.sprint.mission.discodeit.exception.user.PasswordInvalidException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException e, HttpServletRequest request) {

    FieldError fieldError = e.getBindingResult().getFieldError(); // 첫 번째 오류 처리
    DiscodeitException discodeitException = null;
    ErrorCode errorCode;

    switch (fieldError.getField()) {
      case "email":
        errorCode = ErrorCode.EMAIL_INVALID;
        discodeitException = new EmailInvalidException(errorCode,
            Map.of("email", "입력한 이메일: " + request.getParameter("email")));
        break;
      case "nickname":
        errorCode = ErrorCode.NICKNAME_INVALID;
        discodeitException = new NicknameInvalidException(errorCode,
            Map.of("nickname", "입력한 닉네임: " + request.getParameter("nickname")));
        break;
      case "password":
        errorCode = ErrorCode.PASSWORD_INVALID;
        discodeitException = new PasswordInvalidException(errorCode,
            Map.of("password",
                "입력한 패스워드가 너무 짧거나 허용되지 않는 문자가 포함되어있음"));
        break;
      default:
        errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        discodeitException = new DiscodeitException(errorCode);
        break;
    }

    log.error("[ValidationError] {} - field={} value={} | url={} | method={} | ip={}",
        errorCode.getCode(),
        fieldError.getField(),
        fieldError.getRejectedValue(),
        request.getRequestURI(),
        request.getMethod(),
        request.getRemoteAddr()
    );

    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode, discodeitException));
  }

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(DiscodeitException e,
      HttpServletRequest request) {
    ErrorCode errorCode = e.getErrorCode();
    log.error("[CustomException] {} - {} | url={} | method={} | ip={} \n",
        errorCode.getCode(),
        errorCode.getMessage(),
        request.getRequestURI(),
        request.getMethod(),
        request.getRemoteAddr()
        , e  // stack trace 포함
    );
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode, e));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e,
      HttpServletRequest request) {
    log.error("[Exception] {} | url={} | method={} | ip={}",
        e.getMessage(),
        request.getRequestURI(),
        request.getMethod(),
        request.getRemoteAddr()
        , e  // stack trace 포함
    );
    ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode));
  }
}
