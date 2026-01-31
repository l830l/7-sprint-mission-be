package com.sprint.mission.discodeit.domain.auth.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class InvalidPasswordException extends AuthException {

    public InvalidPasswordException(ErrorCode errorCode,
                                    Map<String, String> details) {
        super(errorCode, details);
    }

    public InvalidPasswordException(ErrorCode errorCode) {
        super(errorCode);
    }
}
