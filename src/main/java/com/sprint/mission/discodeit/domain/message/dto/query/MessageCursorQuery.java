package com.sprint.mission.discodeit.domain.message.dto.query;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageCursorQuery(
        @NotNull
        UUID channelId,
        LocalDateTime cursor,                  // 주 커서 값
        UUID after,                            // 보조 커서 값
        Integer limit,                         // 한 페이지 당 보이는 갯수
        String keyword                          // 검색 키워드
) {
}