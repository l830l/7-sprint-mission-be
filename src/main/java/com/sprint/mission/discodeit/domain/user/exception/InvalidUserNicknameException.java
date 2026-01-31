package com.sprint.mission.discodeit.domain.user.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class InvalidUserNicknameException extends UserException {

    public InvalidUserNicknameException(ErrorCode errorCode,
                                        Map<String, String> details) {
        super(errorCode, details);
    }

    public InvalidUserNicknameException(ErrorCode errorCode) {
        super(errorCode);
    }
}
