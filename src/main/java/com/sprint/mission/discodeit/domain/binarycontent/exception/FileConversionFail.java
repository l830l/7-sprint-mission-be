package com.sprint.mission.discodeit.domain.binarycontent.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class FileConversionFail extends BinaryContentException {

    public FileConversionFail(ErrorCode errorCode,
                              Map<String, String> details) {
        super(errorCode, details);
    }

    public FileConversionFail(ErrorCode errorCode) {
        super(errorCode);
    }
}
