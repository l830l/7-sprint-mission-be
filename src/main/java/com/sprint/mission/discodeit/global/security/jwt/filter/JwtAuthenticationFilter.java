package com.sprint.mission.discodeit.global.security.jwt.filter;

import com.sprint.mission.discodeit.global.security.jwt.provider.JwtTokenProvider;
import com.sprint.mission.discodeit.global.security.user.detail.DiscodeitUserDetails;
import com.sprint.mission.discodeit.global.security.user.detail.DiscodeitUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final DiscodeitUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (!hasBearerToken(authorizationHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authorizationHeader.substring(BEARER_PREFIX.length());

        if (!jwtTokenProvider.validateAccessToken(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        String nickname = jwtTokenProvider.getNickname(accessToken);
        DiscodeitUserDetails userDetails = (DiscodeitUserDetails) userDetailsService.loadUserByUsername(nickname);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private boolean hasBearerToken(String authorizationHeader) {
        return authorizationHeader != null
                && authorizationHeader.startsWith(BEARER_PREFIX);
    }
}
