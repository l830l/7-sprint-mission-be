package com.sprint.mission.discodeit.domain.message.facade;

import com.sprint.mission.discodeit.global.dto.response.PageResponse;
import com.sprint.mission.discodeit.domain.message.dto.response.MessageViewRes;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.mapper.MessageMapper;
import com.sprint.mission.discodeit.global.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.domain.message.service.QueryMessageService;
import com.sprint.mission.discodeit.domain.message.vo.MessageCursor;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageOverviewFacade {

    private final QueryMessageService queryMessageService;

    //메세지 전체 조회
    @Transactional(readOnly = true)
    public PageResponse<MessageViewRes> findAllByChannelId(
            UUID channelId, String cursor, int size) {
        Slice<Message> slice = queryMessageService.getRecentMessages(channelId, cursor, size);
        Slice<MessageViewRes> dtoSlice = slice.map(MessageMapper::toResDto);

        String nextCursor = null;
        if (slice.hasNext() && !slice.isEmpty()) {
            Message lastMessage = slice.getContent().get(slice.getContent().size() - 1);
            nextCursor = new MessageCursor(lastMessage.getCreatedAt(), lastMessage.getId()
            ).toPagingCursor();
        }

        return PageResponseMapper.fromSlice(dtoSlice, nextCursor);
    }
}
