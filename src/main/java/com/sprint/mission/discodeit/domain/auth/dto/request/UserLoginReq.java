package com.sprint.mission.discodeit.domain.auth.dto.request;

import com.sprint.mission.discodeit.global.dto.Sanitizable;
import jakarta.validation.constraints.NotBlank;

public record UserLoginReq(
        @NotBlank String nickname,
        @NotBlank String password
) implements Sanitizable {

    @Override
    public Object toLoggingDTO() {
        return new UserLoginReq(nickname, "*****");
    }
}
