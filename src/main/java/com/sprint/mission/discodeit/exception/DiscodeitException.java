package com.sprint.mission.discodeit.exception;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

  private final LocalDateTime createdAt;
  private final ErrorCode errorCode;
  private final Map<String, String> details;

  public DiscodeitException(ErrorCode errorCode, Map<String, String> details) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.details = details;
    createdAt = LocalDateTime.now();
  }

  public DiscodeitException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.details = null;
    createdAt = LocalDateTime.now();
  }
}
