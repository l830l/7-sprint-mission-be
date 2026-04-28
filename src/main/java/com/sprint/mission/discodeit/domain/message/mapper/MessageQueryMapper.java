package com.sprint.mission.discodeit.domain.message.mapper;

import com.sprint.mission.discodeit.domain.message.dto.query.MessageCursorQuery;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCursorReq;

public class MessageQueryMapper {
    public static MessageCursorQuery toQuery(MessageCursorReq req) {
        return new MessageCursorQuery(
                req.channelId(),
                req.cursor(),
                req.after(),
                req.size(),
                req.keyword()
        );
    }
}
