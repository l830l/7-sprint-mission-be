package com.sprint.mission.discodeit.domain.auth.service;

import com.sprint.mission.discodeit.domain.auth.dto.inner.JwtRefreshResult;
import com.sprint.mission.discodeit.domain.auth.dto.response.JwtRes;
import com.sprint.mission.discodeit.domain.user.dto.response.UserSimpleInfoRes;

public interface AuthRefreshService {
    JwtRefreshResult refresh(String refreshToken);

    String issueRefreshToken(UserSimpleInfoRes userInfo);
}
