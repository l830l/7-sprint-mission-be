package com.sprint.mission.discodeit.global.security.expression;

import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("messageSecurity")
@RequiredArgsConstructor
public class MessageSecurity {
    private final MessageService messageService;
    private final ChannelSecurity channelSecurity;

    public boolean canCreate(UUID speakerId, UUID loginUserId) {
        return speakerId.equals(loginUserId);
    }

    public boolean canUpdate(UUID messageId, UUID loginUserId) {
        Message message = messageService.findById(messageId);
        return isSpeaker(message, loginUserId);
    }

    public boolean canDelete(UUID messageId, UUID loginUserId) {
        Message message = messageService.findById(messageId);
        UUID channelId = message.getChannel().getId();

        return isSpeaker(message, loginUserId)
                || channelSecurity.isChannelOwner(channelId, loginUserId);
    }

    private boolean isSpeaker(Message message, UUID loginUserId) {
        return message.getSpeaker().getId().equals(loginUserId);
    }
}
