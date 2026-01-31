package com.sprint.mission.discodeit.domain.channel.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChannelUpdateReq(
        @NotBlank String name,
        String description
) {

}
