package com.sprint.mission.discodeit.domain.user.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class UserAlreadyExistsException extends UserException {

    public UserAlreadyExistsException(ErrorCode errorCode,
                                      Map<String, String> details) {
        super(errorCode, details);
    }

    public UserAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
