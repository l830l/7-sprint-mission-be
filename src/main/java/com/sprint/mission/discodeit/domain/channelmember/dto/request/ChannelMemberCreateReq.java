package com.sprint.mission.discodeit.domain.channelmember.dto.request;

import com.sprint.mission.discodeit.domain.channelmember.entity.ChannelMemberRole;

import java.util.UUID;

public record ChannelMemberCreateReq(
        UUID userId,
        UUID channelId,
        ChannelMemberRole role
) {

}
