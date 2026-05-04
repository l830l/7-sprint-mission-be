package com.sprint.mission.discodeit.global.security.util;

import com.sprint.mission.discodeit.domain.user.dto.response.UserSimpleInfoRes;
import com.sprint.mission.discodeit.global.security.user.detail.DiscodeitUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("loginUser")
public class LoginUser {
    private DiscodeitUserDetails getPrincipal() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        return (DiscodeitUserDetails) authentication.getPrincipal();
    }

    public UserSimpleInfoRes userInfo() {
        return getPrincipal().getUserInfo();
    }

    public UUID userId() {
        return userInfo().userId();
    }
}
