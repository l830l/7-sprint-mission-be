package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ChannelPrivateCannotModifyException extends ChannelException {

  public ChannelPrivateCannotModifyException(
      ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }

  public ChannelPrivateCannotModifyException(ErrorCode errorCode) {
    super(errorCode);
  }
}
