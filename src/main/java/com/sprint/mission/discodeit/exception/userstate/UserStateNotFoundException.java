package com.sprint.mission.discodeit.exception.userstate;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserStateNotFoundException extends UserStateException {

  public UserStateNotFoundException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }

  public UserStateNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
