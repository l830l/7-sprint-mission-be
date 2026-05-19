package com.sprint.mission.discodeit.global.security.jwt.registry;

import java.util.UUID;

public interface JwtRegistry {
    void registerJwtInfo(JwtInfo jwtInfo);

    void invalidateJwtInfoByUserId(UUID userId);

    void invalidateJwtInfoByRefreshToken(String refreshToken);

    boolean hasActiveJwtInfoByUserId(UUID userId);

    boolean hasActiveJwtInfoByAccessToken(String accessToken);

    boolean hasActiveJwtInfoByRefreshToken(String refreshToken);

    void rotateJwtInfo(String oldRefreshToken, JwtInfo newJwtInfo);

    void clearExpiredJwtInfo();
}
