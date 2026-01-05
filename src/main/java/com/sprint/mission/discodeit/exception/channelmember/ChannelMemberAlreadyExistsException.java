package com.sprint.mission.discodeit.exception.channelmember;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ChannelMemberAlreadyExistsException extends ChannelMemberException {

  public ChannelMemberAlreadyExistsException(
      ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }

  public ChannelMemberAlreadyExistsException(ErrorCode errorCode) {
    super(errorCode);
  }
}
