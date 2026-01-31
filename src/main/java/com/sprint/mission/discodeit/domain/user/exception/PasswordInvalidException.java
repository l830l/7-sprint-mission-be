package com.sprint.mission.discodeit.domain.user.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class PasswordInvalidException extends UserException {

    public PasswordInvalidException(ErrorCode errorCode,
                                    Map<String, String> details) {
        super(errorCode, details);
    }

    public PasswordInvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
