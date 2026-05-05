package com.sprint.mission.discodeit.global.security.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class JwtClaimExtractFailException extends JwtException {
    public JwtClaimExtractFailException(ErrorCode errorCode, Map<String, String> details) {
        super(errorCode, details);
    }

    public JwtClaimExtractFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
