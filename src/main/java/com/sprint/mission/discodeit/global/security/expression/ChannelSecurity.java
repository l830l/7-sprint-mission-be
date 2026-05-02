package com.sprint.mission.discodeit.global.security.expression;

import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import com.sprint.mission.discodeit.domain.channelmember.entity.ChannelMemberRole;
import com.sprint.mission.discodeit.domain.channelmember.service.ChannelMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChannelSecurity {
    private final ChannelService channelService;
    private final ChannelMemberService channelMemberService;

    public boolean isPublicChannel(UUID channelId) {
        Channel channel = channelService.findById(channelId);
        return channel.getPublicType() == ChannelType.PUBLIC;
    }

    public boolean isChannelOwner(UUID channelId, UUID userId) {
        return channelMemberService.findAllByChannelId(channelId).stream()
                .anyMatch(channelMember ->
                        channelMember.getUser().getId().equals(userId)
                                && channelMember.getRole() == ChannelMemberRole.OWNER
                );
    }
}
