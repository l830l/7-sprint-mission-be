package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class InvalidUserNicknameException extends UserException {

  public InvalidUserNicknameException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }

  public InvalidUserNicknameException(ErrorCode errorCode) {
    super(errorCode);
  }
}
