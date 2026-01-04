package com.sprint.mission.discodeit.dto.channel.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChannelPrivateInfoRes(
    UUID channelId,
    String type,
    UUID managerId,
    String name,
    LocalDateTime lastMessageTime
) implements ChannelInfoRes {

}
