package com.sprint.mission.discodeit.domain.auth.exception;

import com.sprint.mission.discodeit.global.exception.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class AuthException extends DiscodeitException {

    public AuthException(ErrorCode errorCode, Map<String, String> details) {
        super(errorCode, details);
    }

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
