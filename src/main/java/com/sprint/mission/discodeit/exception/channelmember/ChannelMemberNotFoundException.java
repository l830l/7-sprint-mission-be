package com.sprint.mission.discodeit.exception.channelmember;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ChannelMemberNotFoundException extends ChannelMemberException {

  public ChannelMemberNotFoundException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }

  public ChannelMemberNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
