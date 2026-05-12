package com.sprint.mission.discodeit.global.security.handler;

import com.sprint.mission.discodeit.domain.auth.cookie.RefreshTokenCookieProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {
    private final RefreshTokenCookieProvider refreshTokenCookieProvider;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        refreshTokenCookieProvider.deleteRefreshTokenCookie(response);
    }
}
