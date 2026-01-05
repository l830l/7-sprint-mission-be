package com.sprint.mission.discodeit.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
    LocalDateTime timestamp,
    String code,
    String message,
    Map<String, String> details,
    String exceptionType,
    int httpStatus
) {

  public static ErrorResponse from(ErrorCode errorCode, DiscodeitException e) {
    return new ErrorResponse(
        LocalDateTime.now(),
        errorCode.getCode(),
        errorCode.getMessage(),
        e.getDetails(),
        e.getClass().getSimpleName(),
        errorCode.getStatus().value()
    );
  }

  public static ErrorResponse from(ErrorCode errorCode) {
    return new ErrorResponse(
        LocalDateTime.now(),
        errorCode.getCode(),
        errorCode.getMessage(),
        null,
        "Exception",
        errorCode.getStatus().value()
    );
  }
}
