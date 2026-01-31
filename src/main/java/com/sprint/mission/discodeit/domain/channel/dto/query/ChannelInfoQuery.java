package com.sprint.mission.discodeit.domain.channel.dto.query;

import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChannelInfoQuery {

    private UUID channelId;
    private String name;
    private String description;
    private ChannelType publicType;
    private UUID managerId;
    private LocalDateTime lastMessageTime;
}
