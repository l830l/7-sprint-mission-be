package com.sprint.mission.discodeit.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.auth.dto.response.JwtRes;
import com.sprint.mission.discodeit.domain.user.dto.response.UserSimpleInfoRes;
import com.sprint.mission.discodeit.global.properties.JwtProperties;
import com.sprint.mission.discodeit.global.security.jwt.JwtTokenProvider;
import com.sprint.mission.discodeit.global.security.user.detail.DiscodeitUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {
    private static final String REFRESH_TOKEN_COOKIE_NAME = "REFRESH_TOKEN";

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();
        UserSimpleInfoRes userSimpleInfoRes = userDetails.getUserInfo();

        String accessToken = jwtTokenProvider.createAccessToken(userSimpleInfoRes);
        String refreshToken = jwtTokenProvider.createRefreshToken(userSimpleInfoRes);

        response.addCookie(getRefreshTokenCookie(refreshToken));
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        JwtRes jwtRes = new JwtRes(userSimpleInfoRes, accessToken);
        objectMapper.writeValue(response.getWriter(), jwtRes);
    }

    // refresh token 쿠키
    private Cookie getRefreshTokenCookie(String refreshToken) {
        Cookie refreshTokenCookie = new Cookie(
                REFRESH_TOKEN_COOKIE_NAME, refreshToken);

        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // TODO : Https 에서 True 로 변경
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(
                Math.toIntExact(jwtProperties.refreshTokenExpirationSeconds())
        );

        return refreshTokenCookie;
    }
}
