package com.sprint.mission.discodeit.repository.query;

import com.sprint.mission.discodeit.entity.Message;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MessageRepositoryCustom {

  Slice<Message> findFirstPage(UUID channelId, Pageable pageable);

  Slice<Message> findNextPage(
      UUID channelId, LocalDateTime cursorCreatedAt, UUID cursorMessageId, Pageable pageable);
}
