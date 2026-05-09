package com.sprint.mission.discodeit.domain.auth.mapper;

import com.sprint.mission.discodeit.domain.auth.dto.response.StoredRefreshTokenRes;
import com.sprint.mission.discodeit.domain.auth.entity.RefreshToken;

public class RefreshTokenMapper {
    public static StoredRefreshTokenRes toResDto(RefreshToken refreshToken) {
        return new StoredRefreshTokenRes(
                refreshToken.getUserId(),
                refreshToken.getTokenHash(),
                refreshToken.getExpiresAt(),
                refreshToken.isRevoked()
        );
    }
}
