package com.sprint.mission.discodeit.domain.message.service;

import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.message.dto.query.MessageCursorQuery;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(Message message);

    void update(UUID id, String content, List<BinaryContent> attachments);

    void delete(UUID id);

    Message findById(UUID id);

    List<Message> findAllByChannelId(UUID channelId);

    Slice<Message> getMessages(MessageCursorQuery query);
}
