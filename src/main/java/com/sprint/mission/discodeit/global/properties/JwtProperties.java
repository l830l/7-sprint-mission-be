package com.sprint.mission.discodeit.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "discodeit.jwt")
public record JwtProperties(
        String secret,
        long accessTokenExpirationSeconds,
        long refreshTokenExpirationSeconds,
        RefreshCookie refreshCookie
) {
    public record RefreshCookie(
            String name,
            String path,
            boolean httpOnly,
            boolean secure,
            String sameSite
    ) {
    }
}