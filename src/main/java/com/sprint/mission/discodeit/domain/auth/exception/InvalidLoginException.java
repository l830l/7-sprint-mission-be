package com.sprint.mission.discodeit.domain.auth.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class InvalidLoginException extends AuthException {
    public InvalidLoginException(ErrorCode errorCode,
                                 Map<String, String> details) {
        super(errorCode, details);
    }

    public InvalidLoginException(ErrorCode errorCode) {
        super(errorCode);
    }
}
