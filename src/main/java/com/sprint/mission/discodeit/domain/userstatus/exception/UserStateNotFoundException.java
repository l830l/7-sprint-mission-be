package com.sprint.mission.discodeit.domain.userstatus.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class UserStateNotFoundException extends UserStateException {

    public UserStateNotFoundException(ErrorCode errorCode,
                                      Map<String, String> details) {
        super(errorCode, details);
    }

    public UserStateNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}