package com.sprint.mission.discodeit.domain.auth.dto.response;

public record VerifyCodeRes(
        boolean isVerified,
        String message) {

}
