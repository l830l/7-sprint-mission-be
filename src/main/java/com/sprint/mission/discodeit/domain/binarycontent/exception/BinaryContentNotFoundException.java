package com.sprint.mission.discodeit.domain.binarycontent.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class BinaryContentNotFoundException extends BinaryContentException {

    public BinaryContentNotFoundException(ErrorCode errorCode,
                                          Map<String, String> details) {
        super(errorCode, details);
    }

    public BinaryContentNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
