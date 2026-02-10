package com.sprint.mission.discodeit.domain.channel.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ChannelPrivateInfoRes(
        UUID channelId,
        String type,
        UUID managerId,
        List<UUID> userIds,
        String name,
        LocalDateTime lastMessageTime
) implements ChannelInfoRes {

}
