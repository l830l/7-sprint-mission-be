package com.sprint.mission.discodeit.loggin.support;

import com.sprint.mission.discodeit.dto.common.Sanitizable;
import com.sprint.mission.discodeit.loggin.sanitizer.ArgSanitizer;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogArgumentSanitizer {

  private final List<ArgSanitizer> sanitizers;

  //매개변수 있는지 검사 해서 있으면 매개변수 처리 함수 호출
  public List<?> sanitizeArgs(Object[] args) {
    if (args == null || args.length == 0) {
      return List.of();
    }
    return Arrays.stream(args)
        .map(this::sanitizeArg)
        .toList();
  }

  //매개변수 처리 함수 : multipartfile, password 관련 필드 없애기
  private Object sanitizeArg(Object arg) {
    if (arg instanceof Sanitizable<?> s) {
      return s.toLoggingDTO();
    }
    for (ArgSanitizer sanitizer : sanitizers) {
      if (sanitizer.isFilterCase(arg)) {
        return sanitizer.sanitize(arg);
      }
    }

    return arg;
  }
}
