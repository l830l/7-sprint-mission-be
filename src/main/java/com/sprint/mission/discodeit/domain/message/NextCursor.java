package com.sprint.mission.discodeit.domain.message;

import com.sprint.mission.discodeit.domain.message.entity.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class NextCursor {
    private final LocalDateTime cursor;
    private final UUID after;

    public static NextCursor from(Slice<Message> slice) {
        if (!slice.hasNext() || slice.getContent().isEmpty()) {
            return new NextCursor(null, null);
        }
        Message lastMessage = slice.getContent().get(slice.getContent().size() - 1);

        return new NextCursor(lastMessage.getCreatedAt(), lastMessage.getId());
    }
}
