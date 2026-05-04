package com.sprint.mission.discodeit.domain.auth.dto.request;

import com.sprint.mission.discodeit.domain.user.entity.UserRole;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserRoleUpdateReq(
        @NotNull
        UUID userId,
        @NotNull
        UserRole userRole
) {
}
