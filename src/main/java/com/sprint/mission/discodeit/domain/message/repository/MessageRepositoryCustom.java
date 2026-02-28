package com.sprint.mission.discodeit.domain.message.repository;

import com.sprint.mission.discodeit.domain.message.dto.query.MessageCursorQuery;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import org.springframework.data.domain.Slice;

public interface MessageRepositoryCustom {

    Slice<Message> findAllByCursor(MessageCursorQuery query);
}