package com.sprint.mission.discodeit.domain.user.service;

import com.sprint.mission.discodeit.global.security.user.detail.DiscodeitUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserSessionService {
    private final SessionRegistry sessionRegistry;

    // 하나라도 세션이 살아있으면 온라인 처리
    public boolean isOnline(UUID userId) {
        return findUserDetails(userId)
                .anyMatch(principal ->
                        !sessionRegistry.getAllSessions(principal, false).isEmpty()
                );
    }

    // 특정 userId 로 로그인 중인 세션들을 SessionRegistry 에서 찾아서 모두 "만료 예정"을 표시
    public void expireSessions(UUID userId) {
        findUserDetails(userId)
                .forEach(principal ->
                        sessionRegistry.getAllSessions(principal, false)
                                .forEach(SessionInformation::expireNow)
                );
    }

    // 로그인 중인 사용자들 중에 userId 와 같은 사용자의 DiscodeitUserDetails을 가져옴
    // NOTE: 유저는 하나이되 미래에는 세션이 여러 개 일 수 있음
    // FIXME: 로그인 중인 사용자가 많을 경우 Role 한번 바꾸면 서버에 부하가 생길 수 있음
    private Stream<DiscodeitUserDetails> findUserDetails(UUID userId) {
        return sessionRegistry.getAllPrincipals().stream()
                .filter(DiscodeitUserDetails.class::isInstance)
                .map(DiscodeitUserDetails.class::cast)
                .filter(principal -> principal.getUserInfo().userId().equals(userId));
    }
}
