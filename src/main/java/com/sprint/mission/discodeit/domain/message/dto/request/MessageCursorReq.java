package com.sprint.mission.discodeit.domain.message.dto.request;

import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageCursorReq(
        UUID channelId,
        LocalDateTime cursor,
        UUID after,
        Integer size,
        String keyword
) {
    public MessageCursorReq {
        if (size == null) size = 50;
    }

    public String keywordValue() {
        return StringUtils.hasText(keyword) ? keyword.trim() : null;
    }
}