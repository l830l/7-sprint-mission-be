package com.sprint.mission.discodeit.domain.user.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class EmailInvalidException extends UserException {

    public EmailInvalidException(ErrorCode errorCode,
                                 Map<String, String> details) {
        super(errorCode, details);
    }

    public EmailInvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
