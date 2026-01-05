package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class FileConversionFail extends BinaryContentException {

  public FileConversionFail(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }

  public FileConversionFail(ErrorCode errorCode) {
    super(errorCode);
  }
}
