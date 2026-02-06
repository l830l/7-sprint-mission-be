package com.sprint.mission.discodeit.domain.message.mapper;

import com.sprint.mission.discodeit.domain.message.dto.query.MessageCursorQuery;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCursorReq;
import org.springframework.stereotype.Component;

@Component
public class MessageQueryMapper {
    public MessageCursorQuery toQuery(MessageCursorReq req) {
        return new MessageCursorQuery(
                req.cursor(),
                req.after(),
                req.limit(),
                req.keyword()
        );
    }
}
