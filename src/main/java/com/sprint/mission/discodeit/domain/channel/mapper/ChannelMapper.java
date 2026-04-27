package com.sprint.mission.discodeit.domain.channel.mapper;

import com.sprint.mission.discodeit.domain.channel.dto.query.ChannelInfoQuery;
import com.sprint.mission.discodeit.domain.channel.dto.response.ChannelInfoRes;
import com.sprint.mission.discodeit.domain.channel.dto.response.ChannelPrivateInfoRes;
import com.sprint.mission.discodeit.domain.channel.dto.response.ChannelPublicInfoRes;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ChannelMapper {

    public static ChannelInfoRes toPublicResDto(
            ChannelInfoQuery channel) {
        return new ChannelPublicInfoRes(
                channel.getChannelId(),
                channel.getPublicType().getValue(),
                channel.getManagerId(),
                channel.getName(),
                channel.getDescription(),
                channel.getLastMessageTime()
        );
    }

    public static ChannelInfoRes toPrivateResDto(
            ChannelInfoQuery channel, List<UUID> userIds) {
        return new ChannelPrivateInfoRes(
                channel.getChannelId(),
                channel.getPublicType().getValue(),
                channel.getManagerId(),
                userIds,
                channel.getName(),
                channel.getLastMessageTime()
        );
    }
}
