package com.sprint.mission.discodeit.exception.userstate;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserStateException extends DiscodeitException {

  public UserStateException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }

  public UserStateException(ErrorCode errorCode) {
    super(errorCode);
  }
}
