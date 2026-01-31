package com.sprint.mission.discodeit.domain.auth.service;

import com.sprint.mission.discodeit.domain.auth.dto.response.VerifyCodeRes;

public interface AuthService {

    void sendEmailCode(String email);

    VerifyCodeRes verifyEmailCode(String email, String code);
}
