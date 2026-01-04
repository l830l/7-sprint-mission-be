package com.sprint.mission.discodeit.vo;

import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.UUID;

public record MessageCursor(
    LocalDateTime createdAt,
    UUID messageId
) {

  private static final String DELIMITER = "_";

  //들어온 문자열로 VO형식을 반환함
  public static MessageCursor fromPagingCursor(String cursor) {
    if (cursor == null || cursor.isBlank()) {
      return null;
    }

    String[] parts = cursor.split(DELIMITER);
    if (parts.length != 2) {
      throw new CustomException(ErrorCode.INVALID_CURSOR);
    }

    try {
      LocalDateTime createdAt = LocalDateTime.parse(parts[0]);
      UUID messageId = UUID.fromString(parts[1]);
      return new MessageCursor(createdAt, messageId);
    } catch (Exception e) {
      throw new CustomException(ErrorCode.INVALID_CURSOR);
    }
  }

  //커서 형식에 맞게 문자열 생성
  public String toPagingCursor() {
    return createdAt + DELIMITER + messageId;
  }
}
