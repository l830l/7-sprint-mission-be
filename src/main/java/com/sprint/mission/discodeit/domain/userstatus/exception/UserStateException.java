package com.sprint.mission.discodeit.domain.userstatus.exception;

import com.sprint.mission.discodeit.global.exception.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class UserStateException extends DiscodeitException {

    public UserStateException(ErrorCode errorCode,
                              Map<String, String> details) {
        super(errorCode, details);
    }

    public UserStateException(ErrorCode errorCode) {
        super(errorCode);
    }
}