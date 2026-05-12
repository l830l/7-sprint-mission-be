package com.sprint.mission.discodeit.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.auth.cookie.RefreshTokenCookieProvider;
import com.sprint.mission.discodeit.domain.auth.dto.response.JwtRes;
import com.sprint.mission.discodeit.domain.auth.service.AuthRefreshService;
import com.sprint.mission.discodeit.domain.user.dto.response.UserSimpleInfoRes;
import com.sprint.mission.discodeit.global.security.jwt.provider.JwtTokenProvider;
import com.sprint.mission.discodeit.global.security.user.detail.DiscodeitUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenCookieProvider refreshTokenCookieProvider;
    private final AuthRefreshService authRefreshService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();
        UserSimpleInfoRes userInfo = userDetails.getUserInfo();

        String accessToken = jwtTokenProvider.createAccessToken(userInfo);
        String refreshToken = authRefreshService.issueRefreshToken(userInfo);

        refreshTokenCookieProvider.addRefreshCookie(response, refreshToken);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        JwtRes jwtRes = new JwtRes(userInfo, accessToken);
        objectMapper.writeValue(response.getWriter(), jwtRes);
    }
}
