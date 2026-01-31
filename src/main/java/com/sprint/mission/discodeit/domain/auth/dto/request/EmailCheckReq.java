package com.sprint.mission.discodeit.domain.auth.dto.request;

import jakarta.validation.constraints.Pattern;

public record EmailCheckReq(
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")
        String email) {

}
