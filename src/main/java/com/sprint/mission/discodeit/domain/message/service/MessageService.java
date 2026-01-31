package com.sprint.mission.discodeit.domain.message.service;

import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.message.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    List<Message> findAllByChannelId(UUID channelId);

    Message create(Message message);

    void update(UUID id, String content, List<BinaryContent> attachments);

    void delete(UUID id);

    Message findById(UUID id);
}
