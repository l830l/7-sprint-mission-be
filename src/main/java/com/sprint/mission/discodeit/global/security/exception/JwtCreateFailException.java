package com.sprint.mission.discodeit.global.security.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class JwtCreateFailException extends JwtException {
    public JwtCreateFailException(ErrorCode errorCode, Map<String, String> details) {
        super(errorCode, details);
    }

    public JwtCreateFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
