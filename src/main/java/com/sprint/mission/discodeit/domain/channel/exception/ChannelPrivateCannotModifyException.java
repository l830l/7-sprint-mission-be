package com.sprint.mission.discodeit.domain.channel.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class ChannelPrivateCannotModifyException extends ChannelException {

    public ChannelPrivateCannotModifyException(
            ErrorCode errorCode,
            Map<String, String> details) {
        super(errorCode, details);
    }

    public ChannelPrivateCannotModifyException(ErrorCode errorCode) {
        super(errorCode);
    }
}
