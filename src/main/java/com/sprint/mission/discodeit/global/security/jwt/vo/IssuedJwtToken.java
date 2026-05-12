package com.sprint.mission.discodeit.global.security.jwt.vo;

import java.time.Instant;

public record IssuedJwtToken(
        String accessToken,
        String refreshToken,
        String accessTokenHash,
        String refreshTokenHash,
        Instant accessTokenExpiresAt,
        Instant refreshTokenExpiresAt
) {
}
