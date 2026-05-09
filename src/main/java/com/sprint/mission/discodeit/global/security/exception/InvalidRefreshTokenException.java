package com.sprint.mission.discodeit.global.security.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class InvalidRefreshTokenException extends JwtException {
    public InvalidRefreshTokenException(ErrorCode errorCode, Map<String, String> details) {
        super(errorCode, details);
    }

    public InvalidRefreshTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
