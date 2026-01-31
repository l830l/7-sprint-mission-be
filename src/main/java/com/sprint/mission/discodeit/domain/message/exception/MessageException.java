package com.sprint.mission.discodeit.domain.message.exception;

import com.sprint.mission.discodeit.global.exception.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class MessageException extends DiscodeitException {

    public MessageException(ErrorCode errorCode,
                            Map<String, String> details) {
        super(errorCode, details);
    }

    public MessageException(ErrorCode errorCode) {
        super(errorCode);
    }
}
