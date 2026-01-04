package com.sprint.mission.discodeit.loggin.sanitizer;

public interface ArgSanitizer {

  boolean isFilterCase(Object arg);

  Object sanitize(Object arg);
}
