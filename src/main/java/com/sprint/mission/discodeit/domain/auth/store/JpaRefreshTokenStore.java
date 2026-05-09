package com.sprint.mission.discodeit.domain.auth.store;

import com.sprint.mission.discodeit.domain.auth.dto.response.StoredRefreshTokenRes;
import com.sprint.mission.discodeit.domain.auth.entity.RefreshToken;
import com.sprint.mission.discodeit.domain.auth.mapper.RefreshTokenMapper;
import com.sprint.mission.discodeit.domain.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JpaRefreshTokenStore implements RefreshTokenStore {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void save(UUID userId, String tokenHash, Instant expireAt) {
        refreshTokenRepository.save(
                RefreshToken.create(userId, tokenHash, expireAt)
        );
    }

    @Override
    public Optional<StoredRefreshTokenRes> findByTokenHash(String tokenHash) {
        return refreshTokenRepository.findByTokenHash(tokenHash)
                .map(RefreshTokenMapper::toResDto);

    }

    @Override
    public void revoke(String tokenHash) {
        refreshTokenRepository.findByTokenHash(tokenHash)
                .ifPresent(token -> {
                    token.invalidate();
                    refreshTokenRepository.save(token);
                });
    }

    @Override
    public void revokeAllByUserId(UUID userId) {
        refreshTokenRepository.findAllByUserIdAndRevokedFalse(userId)
                .forEach(RefreshToken::invalidate);
    }
}
