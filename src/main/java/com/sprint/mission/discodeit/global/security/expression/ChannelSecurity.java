package com.sprint.mission.discodeit.global.security.expression;

import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import com.sprint.mission.discodeit.domain.channelmember.service.ChannelMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("channelSecurity")
@RequiredArgsConstructor
public class ChannelSecurity {
    private final ChannelService channelService;
    private final ChannelMemberService channelMemberService;

    public boolean canCreate(UUID managerId, UUID loginId) {
        return managerId.equals(loginId);
    }

    public boolean canUpdate(UUID channelId, UUID userId) {
        return isPublicChannel(channelId) && isChannelOwner(channelId, userId);
    }

    public boolean canDelete(UUID channelId, UUID userId) {
        return isChannelOwner(channelId, userId);
    }

    private boolean isPublicChannel(UUID channelId) {
        Channel channel = channelService.findById(channelId);
        return channel.getPublicType() == ChannelType.PUBLIC;
    }

    private boolean isChannelMember(UUID channelId, UUID userId) {
        return channelMemberService.isMember(channelId, userId);
    }

    private boolean isChannelOwner(UUID channelId, UUID userId) {
        return channelMemberService.isManager(channelId, userId);
    }
}
