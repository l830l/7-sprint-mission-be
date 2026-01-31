package com.sprint.mission.discodeit.domain.userstatus.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class UserStateMissingException extends UserStateException {

    public UserStateMissingException(ErrorCode errorCode,
                                     Map<String, String> details) {
        super(errorCode, details);
    }

    public UserStateMissingException(ErrorCode errorCode) {
        super(errorCode);
    }
}