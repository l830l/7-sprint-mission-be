package com.sprint.mission.discodeit.domain.user.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class UserNotFoundException extends UserException {

    public UserNotFoundException(ErrorCode errorCode,
                                 Map<String, String> details) {
        super(errorCode, details);
    }

    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
