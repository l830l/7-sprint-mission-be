package com.sprint.mission.discodeit.exception.channelmember;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ChannelMemberException extends DiscodeitException {

  public ChannelMemberException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }

  public ChannelMemberException(ErrorCode errorCode) {
    super(errorCode);
  }
}
