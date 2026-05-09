package com.sprint.mission.discodeit.global.security.jwt.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtClaim {
    USER_ID("userId"),
    NICKNAME("nickname"),
    EMAIL("email"),
    ROLE("role"),
    TOKEN_TYPE("tokenType");

    private final String value;
}
