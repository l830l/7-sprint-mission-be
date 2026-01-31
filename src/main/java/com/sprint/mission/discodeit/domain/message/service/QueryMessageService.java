package com.sprint.mission.discodeit.domain.message.service;

import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.repository.MessageRepositoryCustom;
import com.sprint.mission.discodeit.domain.message.vo.MessageCursor;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QueryMessageService {

    private final MessageRepositoryCustom messageRepositoryCustom;

    public Slice<Message> getRecentMessages(UUID channelId, String cursor, int size) {
        Pageable pageable = PageRequest.of(0, size);

        if (cursor == null) {
            return messageRepositoryCustom.findFirstPage(channelId, pageable);
        }

        MessageCursor messageCursor = MessageCursor.fromPagingCursor(cursor);
        return messageRepositoryCustom.findNextPage(
                channelId, messageCursor.createdAt(), messageCursor.messageId(), pageable);
    }
}
