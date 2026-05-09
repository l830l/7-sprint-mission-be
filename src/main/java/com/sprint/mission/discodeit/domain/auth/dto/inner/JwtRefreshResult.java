package com.sprint.mission.discodeit.domain.auth.dto.inner;

import com.sprint.mission.discodeit.domain.user.dto.response.UserSimpleInfoRes;

public record JwtRefreshResult(
        UserSimpleInfoRes userInfo,
        String accessToken,
        String refreshToken
) {
}
