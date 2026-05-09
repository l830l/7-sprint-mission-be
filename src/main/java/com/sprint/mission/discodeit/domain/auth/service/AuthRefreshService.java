package com.sprint.mission.discodeit.domain.auth.service;

import com.sprint.mission.discodeit.domain.auth.dto.response.JwtRes;
import com.sprint.mission.discodeit.domain.user.dto.response.UserSimpleInfoRes;

public interface AuthRefreshService {
    JwtRes refresh(String refreshToken);

    String issueRefreshToken(UserSimpleInfoRes userInfo);
}
