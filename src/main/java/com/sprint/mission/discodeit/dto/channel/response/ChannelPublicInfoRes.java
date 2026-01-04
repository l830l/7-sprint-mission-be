package com.sprint.mission.discodeit.dto.channel.response;

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
