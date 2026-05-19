package com.sprint.mission.discodeit.global.security.jwt.registry;

import java.time.Instant;
import java.util.UUID;

public record JwtInfo(
        UUID userId,
        String accessTokenHash,
        String refreshTokenHash,
        Instant accessTokenExpiresAt,
        Instant refreshTokenExpiresAt
) {
    public boolean isExpired() {
        return Instant.now().isAfter(refreshTokenExpiresAt);
    }

    public boolean hasAccessTokenHash(String tokenHash) {
        return accessTokenHash.equals(tokenHash);
    }

    public boolean hasRefreshTokenHash(String tokenHash) {
        return refreshTokenHash.equals(tokenHash);
    }
}
