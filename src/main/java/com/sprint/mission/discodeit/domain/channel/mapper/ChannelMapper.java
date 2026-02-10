package com.sprint.mission.discodeit.domain.channel.mapper;

import com.sprint.mission.discodeit.domain.channel.dto.query.ChannelInfoQuery;
import com.sprint.mission.discodeit.domain.channel.dto.response.ChannelInfoRes;
import com.sprint.mission.discodeit.domain.channel.dto.response.ChannelPrivateInfoRes;
import com.sprint.mission.discodeit.domain.channel.dto.response.ChannelPublicInfoRes;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

    public static ChannelInfoRes toPrivateResDto(
            Channel channel,
            UUID managerId,
            List<UUID> userIds,
            LocalDateTime lastMessageTime) {
        return new ChannelPrivateInfoRes(
                channel.getId(),
                channel.getPublicType().getValue(),
                managerId,
                userIds,
                channel.getName(),
                lastMessageTime
        );
    }

    public static ChannelInfoRes toPublicResDto(
            Channel channel,
            UUID managerId,
            LocalDateTime lastMessageTime) {
        return new ChannelPublicInfoRes(
                channel.getId(),
                channel.getPublicType().getValue(),
                managerId,
                channel.getName(),
                channel.getDescription(),
                lastMessageTime
        );
    }

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
