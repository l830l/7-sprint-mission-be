package com.sprint.mission.discodeit.domain.auth.dto.request;

public record VerifyCodeReq(
        String email,
        String code
) {

}
