package com.sprint.mission.discodeit.domain.channelmember.factory;

import com.sprint.mission.discodeit.domain.channelmember.dto.request.ChannelMemberCreateReq;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channelmember.entity.ChannelMember;
import com.sprint.mission.discodeit.domain.channelmember.entity.ChannelMemberRole;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import com.sprint.mission.discodeit.domain.user.service.UserService;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMemberFactory {

    private final UserService userService;
    private final ChannelService channelService;

    public ChannelMember create(UUID userId, UUID channelId, ChannelMemberRole role) {
        User user = userService.findById(userId);
        Channel channel = channelService.findById(channelId);

        return ChannelMember.create(user, channel, role);
    }

    public ChannelMember create(ChannelMemberCreateReq req) {
        return create(req.userId(), req.channelId(), req.role());
    }
}