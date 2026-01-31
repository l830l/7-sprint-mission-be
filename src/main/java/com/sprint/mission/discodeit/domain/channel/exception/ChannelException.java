package com.sprint.mission.discodeit.domain.channel.exception;

import com.sprint.mission.discodeit.global.exception.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class ChannelException extends DiscodeitException {

    public ChannelException(ErrorCode errorCode, Map<String, String> details) {
        super(errorCode, details);
    }

    public ChannelException(ErrorCode errorCode) {
        super(errorCode);
    }
}
