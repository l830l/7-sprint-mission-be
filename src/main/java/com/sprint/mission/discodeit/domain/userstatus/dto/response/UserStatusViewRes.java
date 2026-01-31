package com.sprint.mission.discodeit.domain.userstatus.dto.response;

import java.util.UUID;

public record UserStatusViewRes(
        UUID userId,
        boolean isOnline,
        String lastOfflineAt
) {

}
