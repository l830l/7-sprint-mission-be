package com.sprint.mission.discodeit.global.security.exception;

import com.sprint.mission.discodeit.global.exception.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class JwtException extends DiscodeitException {

    public JwtException(ErrorCode errorCode, Map<String, String> details) {
        super(errorCode, details);
    }

    public JwtException(ErrorCode errorCode) {
        super(errorCode);
    }
}
