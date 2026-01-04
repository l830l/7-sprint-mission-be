package com.sprint.mission.discodeit.facade.message;

import com.sprint.mission.discodeit.dto.common.response.PageResponse;
import com.sprint.mission.discodeit.dto.message.response.MessageViewRes;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.query.MessageRepositoryImpl;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.query.QueryMessageService;
import com.sprint.mission.discodeit.vo.MessageCursor;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageOverviewFacade {

  private final QueryMessageService queryMessageService;

  //메세지 전체 조회
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
