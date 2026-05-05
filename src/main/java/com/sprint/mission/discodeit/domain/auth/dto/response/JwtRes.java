package com.sprint.mission.discodeit.domain.auth.dto.response;

import com.sprint.mission.discodeit.domain.user.dto.response.UserSimpleInfoRes;

public record JwtRes(
        UserSimpleInfoRes userInfo,
        String accessToken
) {
}
