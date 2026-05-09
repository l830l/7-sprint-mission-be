package com.sprint.mission.discodeit.domain.auth.dto.response;

import java.time.Instant;
import java.util.UUID;

public record StoredRefreshTokenRes(
        UUID userId,
        String tokenHash,
        Instant expireAt,
        boolean revoked
) {
    public boolean isExpired() {
        return Instant.now().isAfter(expireAt);
    }

    public boolean isActive() {
        return !revoked && !isExpired();
    }
}
