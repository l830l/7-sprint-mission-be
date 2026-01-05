package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class EmailInvalidException extends UserException {

  public EmailInvalidException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }

  public EmailInvalidException(ErrorCode errorCode) {
    super(errorCode);
  }
}
