package com.sprint.mission.discodeit.domain.auth.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class InvalidNicknameException extends AuthException {

    public InvalidNicknameException(ErrorCode errorCode,
                                    Map<String, String> details) {
        super(errorCode, details);
    }

    public InvalidNicknameException(ErrorCode errorCode) {
        super(errorCode);
    }
}
