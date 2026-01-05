package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class NicknameInvalidException extends UserException {

  public NicknameInvalidException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }

  public NicknameInvalidException(ErrorCode errorCode) {
    super(errorCode);
  }
}
