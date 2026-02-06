package com.sprint.mission.discodeit.domain.message.facade;

import com.sprint.mission.discodeit.domain.message.NextCursor;
import com.sprint.mission.discodeit.domain.message.dto.query.MessageCursorQuery;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCursorReq;
import com.sprint.mission.discodeit.domain.message.dto.response.MessageViewRes;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.mapper.MessageMapper;
import com.sprint.mission.discodeit.domain.message.mapper.MessageQueryMapper;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import com.sprint.mission.discodeit.global.dto.response.PageResponse;
import com.sprint.mission.discodeit.global.mapper.PageResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageOverviewFacade {
    private final MessageService messageService;
    private final MessageQueryMapper messageQueryMapper;

    //메세지 전체 조회
    @Transactional(readOnly = true)
    public PageResponse<MessageViewRes> findAllByChannelId(MessageCursorReq req) {
        MessageCursorQuery query = messageQueryMapper.toQuery(req);
        Slice<Message> slice = messageService.getMessages(query);
        Slice<MessageViewRes> dtoSlice = slice.map(MessageMapper::toResDto);
        NextCursor nextCursor = NextCursor.from(slice);

        return PageResponseMapper.fromSlice(dtoSlice, nextCursor);
    }
}
