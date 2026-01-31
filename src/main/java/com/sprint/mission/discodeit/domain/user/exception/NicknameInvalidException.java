package com.sprint.mission.discodeit.domain.user.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class NicknameInvalidException extends UserException {

    public NicknameInvalidException(ErrorCode errorCode,
                                    Map<String, String> details) {
        super(errorCode, details);
    }

    public NicknameInvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
