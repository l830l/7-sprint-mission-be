package com.sprint.mission.discodeit.global.security.jwt.registry;

import com.sprint.mission.discodeit.global.security.jwt.util.TokenHashUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class InMemoryJwtRegistry implements JwtRegistry {
    private static final int DEFAULT_MAX_ACTIVE_JWT_COUNT = 1;

    private final Map<UUID, Queue<JwtInfo>> jwtMapByUserId = new ConcurrentHashMap<>();
    private final int maxActiveJwtCount;

    public InMemoryJwtRegistry() {
        this.maxActiveJwtCount = DEFAULT_MAX_ACTIVE_JWT_COUNT;
    }

    // 로그인 성공 시 새 JWT 정보 등록
    @Override
    public void registerJwtInfo(JwtInfo jwtInfo) {
        Queue<JwtInfo> jwtInfoQueue = jwtMapByUserId.computeIfAbsent(
                jwtInfo.userId(),
                userId -> new ConcurrentLinkedQueue<>()
        );

        while (jwtInfoQueue.size() >= maxActiveJwtCount) {
            jwtInfoQueue.poll();
        }

        jwtInfoQueue.offer(jwtInfo);
    }

    // 특정 유저의 모든 JWT 정보 삭제
    @Override
    public void invalidateJwtInfoByUserId(UUID userId) {
        jwtMapByUserId.remove(userId);
    }

    // refresh token 으로 해당 로그인 정보를 찾아 삭제
    @Override
    public void invalidateJwtInfoByRefreshToken(String refreshToken) {
        String refreshTokenHash = TokenHashUtils.sha256(refreshToken);

        jwtMapByUserId.values().forEach(
                queue -> queue.removeIf(
                        jwtInfo -> jwtInfo.hasRefreshTokenHash(refreshTokenHash)
                )
        );
    }

    // 해당 유저가 현재 로그인 상태인지 확인
    @Override
    public boolean hasActiveJwtInfoByUserId(UUID userId) {
        Queue<JwtInfo> jwtInformation = jwtMapByUserId.get(userId);

        if (jwtInformation == null) {
            return false;
        }

        return jwtInformation.stream()
                .anyMatch(jwtInfo -> !jwtInfo.isExpired());
    }

    // access token 이 Registry 에 살아있는 토큰인지 확인
    @Override
    public boolean hasActiveJwtInfoByAccessToken(String accessToken) {
        String accessTokenHash = TokenHashUtils.sha256(accessToken);

        return jwtMapByUserId.values().stream()
                .flatMap(Queue::stream)
                .anyMatch(jwtInformation ->
                        jwtInformation.hasAccessTokenHash(accessTokenHash)
                                && !jwtInformation.isExpired()
                );
    }

    // refresh token이 Registry에 살아있는 토큰인지 확인
    @Override
    public boolean hasActiveJwtInfoByRefreshToken(String refreshToken) {
        String refreshTokenHash = TokenHashUtils.sha256(refreshToken);

        return jwtMapByUserId.values().stream()
                .flatMap(Queue::stream)
                .anyMatch(jwtInformation ->
                        jwtInformation.hasRefreshTokenHash(refreshTokenHash)
                                && !jwtInformation.isExpired()
                );
    }

    // 기존 refresh token 정보 삭제. 새 access/refresh token 정보 등록
    @Override
    public void rotateJwtInfo(String oldRefreshToken, JwtInfo newJwtInfo) {
        invalidateJwtInfoByRefreshToken(oldRefreshToken);
        registerJwtInfo(newJwtInfo);
    }

    // 만료된 JWT 정보를 주기적으로 삭제
    @Override
    @Scheduled(fixedDelay = 1000 * 60 * 5)
    public void clearExpiredJwtInfo() {
        for (Queue<JwtInfo> jwtInformation : jwtMapByUserId.values()) {
            jwtInformation.removeIf(JwtInfo::isExpired);
        }

        removeEmptyQueues();
    }

    // 큐가 비어있으면 Map에서 userId 항목 자체를 삭제해.
    private void removeEmptyQueues() {
        Iterator<Map.Entry<UUID, Queue<JwtInfo>>> iterator =
                jwtMapByUserId.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, Queue<JwtInfo>> entry = iterator.next();

            if (entry.getValue().isEmpty()) {
                iterator.remove();
            }
        }
    }
}
