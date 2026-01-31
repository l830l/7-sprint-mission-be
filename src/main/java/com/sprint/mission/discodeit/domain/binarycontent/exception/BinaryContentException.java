package com.sprint.mission.discodeit.domain.binarycontent.exception;

import com.sprint.mission.discodeit.global.exception.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class BinaryContentException extends DiscodeitException {

    public BinaryContentException(ErrorCode errorCode,
                                  Map<String, String> details) {
        super(errorCode, details);
    }

    public BinaryContentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
