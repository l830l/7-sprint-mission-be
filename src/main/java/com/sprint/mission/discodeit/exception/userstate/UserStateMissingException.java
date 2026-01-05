package com.sprint.mission.discodeit.exception.userstate;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserStateMissingException extends UserStateException {

  public UserStateMissingException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }

  public UserStateMissingException(ErrorCode errorCode) {
    super(errorCode);
  }
}
