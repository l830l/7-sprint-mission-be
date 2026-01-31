package com.sprint.mission.discodeit.domain.user.exception;

import com.sprint.mission.discodeit.global.exception.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class UserException extends DiscodeitException {

    public UserException(ErrorCode errorCode,
                         Map<String, String> details) {
        super(errorCode, details);
    }

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
