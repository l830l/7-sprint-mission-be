package com.sprint.mission.discodeit.domain.auth.store;

import com.sprint.mission.discodeit.domain.auth.dto.response.StoredRefreshTokenRes;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenStore {
    void save(UUID userId, String tokenHash, Instant expireAt);

    Optional<StoredRefreshTokenRes> findByTokenHash(String tokenHash);

    void revoke(String tokenHash);

    void revokeAllByUserId(UUID userId);
}
