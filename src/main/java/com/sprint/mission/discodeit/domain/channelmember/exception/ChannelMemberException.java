package com.sprint.mission.discodeit.domain.channelmember.exception;

import com.sprint.mission.discodeit.global.exception.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class ChannelMemberException extends DiscodeitException {

    public ChannelMemberException(ErrorCode errorCode,
                                  Map<String, String> details) {
        super(errorCode, details);
    }

    public ChannelMemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
