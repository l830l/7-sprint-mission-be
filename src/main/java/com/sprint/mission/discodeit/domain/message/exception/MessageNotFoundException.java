package com.sprint.mission.discodeit.domain.message.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class MessageNotFoundException extends MessageException {

    public MessageNotFoundException(ErrorCode errorCode,
                                    Map<String, String> details) {
        super(errorCode, details);
    }

    public MessageNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
