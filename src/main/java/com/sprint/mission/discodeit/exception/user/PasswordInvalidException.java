package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class PasswordInvalidException extends UserException {

  public PasswordInvalidException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }

  public PasswordInvalidException(ErrorCode errorCode) {
    super(errorCode);
  }
}
