package com.sprint.mission.discodeit.domain.channel.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChannelPublicInfoRes(
        UUID channelId,
        String type,
        UUID managerId,
        String name,
        String description,
        LocalDateTime lastMessageTime
) implements ChannelInfoRes {

}
