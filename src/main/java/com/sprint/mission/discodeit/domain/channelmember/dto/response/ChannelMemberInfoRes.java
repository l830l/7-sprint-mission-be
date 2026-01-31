package com.sprint.mission.discodeit.domain.channelmember.dto.response;

import java.util.UUID;

public record ChannelMemberInfoRes(
        String createdAt,
        String updatedAt,
        UUID userId,
        UUID channelId
) {

}
