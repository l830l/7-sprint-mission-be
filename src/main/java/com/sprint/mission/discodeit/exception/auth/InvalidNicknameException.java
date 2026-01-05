package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class InvalidNicknameException extends AuthException {

  public InvalidNicknameException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }

  public InvalidNicknameException(ErrorCode errorCode) {
    super(errorCode);
  }
}
