package com.sprint.mission.discodeit.domain.channel.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class ChannelInvalideTypeException extends ChannelException {
    public ChannelInvalideTypeException(ErrorCode errorCode, Map<String, String> details) {
        super(errorCode, details);
    }

    public ChannelInvalideTypeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
