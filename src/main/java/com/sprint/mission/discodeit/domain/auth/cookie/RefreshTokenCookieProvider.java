package com.sprint.mission.discodeit.domain.auth.cookie;

import com.sprint.mission.discodeit.global.properties.JwtProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class RefreshTokenCookieProvider {
    private final JwtProperties jwtProperties;

    // 리프레쉬 토큰 쿠키에서 추출
    public String extractToken(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        String cookieName = jwtProperties.refreshCookie().name();

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    // 쿠키에 리프레쉬 토큰 셋팅
    public ResponseCookie buildRefreshCookie(String refreshToken) {
        JwtProperties.RefreshCookie cookie = jwtProperties.refreshCookie();

        return ResponseCookie.from(cookie.name(), refreshToken)
                .httpOnly(cookie.httpOnly())
                .secure(cookie.secure())
                .sameSite(cookie.sameSite())
                .path(cookie.path())
                .maxAge(jwtProperties.refreshTokenExpirationSeconds())
                .build();
    }

    // 응답 헤더에 리프레쉬 토큰을 넣기
    public void addRefreshCookie(HttpServletResponse response, String refreshToken) {
        response.addHeader(HttpHeaders.SET_COOKIE, buildRefreshCookie(refreshToken).toString());
    }

    // 쿠키에서 삭제
    private ResponseCookie deleteCookie() {
        JwtProperties.RefreshCookie cookie = jwtProperties.refreshCookie();

        return ResponseCookie.from(cookie.name(), "")
                .httpOnly(cookie.httpOnly())
                .secure(cookie.secure())
                .sameSite(cookie.sameSite())
                .path(cookie.path())
                .maxAge(0)
                .build();
    }

    public void deleteRefreshTokenCookie(HttpServletResponse response) {
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                deleteCookie().toString()
        );
    }
}
