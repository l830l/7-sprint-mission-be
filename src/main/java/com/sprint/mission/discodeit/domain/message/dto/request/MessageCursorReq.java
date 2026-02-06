package com.sprint.mission.discodeit.domain.message.dto.request;

import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageCursorReq(
        LocalDateTime cursor,
        UUID after,
        Integer limit,
        String keyword
) {
    public MessageCursorReq {
        // Todo : 나중에 50으로 바꾸기
        if (limit == null) limit = 5;
    }

    public String keywordValue() {
        return StringUtils.hasText(keyword) ? keyword.trim() : null;
    }
}