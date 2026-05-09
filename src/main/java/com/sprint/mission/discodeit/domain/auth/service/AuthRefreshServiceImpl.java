package com.sprint.mission.discodeit.domain.auth.service;

import com.sprint.mission.discodeit.domain.auth.dto.response.JwtRes;
import com.sprint.mission.discodeit.domain.auth.dto.response.StoredRefreshTokenRes;
import com.sprint.mission.discodeit.domain.auth.store.RefreshTokenStore;
import com.sprint.mission.discodeit.domain.user.dto.response.UserSimpleInfoRes;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.security.exception.InvalidRefreshTokenException;
import com.sprint.mission.discodeit.global.security.jwt.provider.JwtTokenProvider;
import com.sprint.mission.discodeit.global.security.jwt.util.TokenHashUtils;
import com.sprint.mission.discodeit.global.security.jwt.vo.IssuedJwtToken;
import com.sprint.mission.discodeit.global.security.user.detail.DiscodeitUserDetails;
import com.sprint.mission.discodeit.global.security.user.detail.DiscodeitUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthRefreshServiceImpl implements AuthRefreshService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenStore refreshTokenStore;
    private final DiscodeitUserDetailsService userDetailsService;

    // 유저 정보 + 액세스 토큰 발급
    @Override
    @Transactional
    public JwtRes refresh(String refreshToken) {
        validateRefreshToken(refreshToken);
        StoredRefreshTokenRes storedToken = getStoredToken(refreshToken);
        UserSimpleInfoRes userInfo = getUserByToken(refreshToken);
        refreshTokenStore.revoke(storedToken.tokenHash());
        IssuedJwtToken issuedJwtToken = jwtTokenProvider.issueToken(userInfo);
        refreshTokenStore.save(
                userInfo.userId(),
                issuedJwtToken.refreshTokenHash(),
                issuedJwtToken.refreshTokenExpiresAt()
        );

        return new JwtRes(userInfo, issuedJwtToken.accessToken());
    }

    @Override
    @Transactional
    public String issueRefreshToken(UserSimpleInfoRes userInfo) {
        String refreshToken = jwtTokenProvider.createRefreshToken(userInfo);
        String tokenHash = TokenHashUtils.sha256(refreshToken);

        refreshTokenStore.save(
                userInfo.userId(),
                tokenHash,
                jwtTokenProvider.createRefreshTokenExpiresAt()
        );

        return refreshToken;
    }

    // 토큰이 비어있거나 형식이 맞는지 검사
    private void validateRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new InvalidRefreshTokenException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new InvalidRefreshTokenException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    // 예전 토큰 해쉬로 DB 에 있는지, 만료되어있는지 찾기
    private StoredRefreshTokenRes getStoredToken(String refreshToken) {
        String oldTokenHash = TokenHashUtils.sha256(refreshToken);
        StoredRefreshTokenRes storedToken = refreshTokenStore.findByTokenHash(oldTokenHash)
                .orElseThrow(() -> new InvalidRefreshTokenException(ErrorCode.INVALID_REFRESH_TOKEN));
        if (!storedToken.isActive()) {
            throw new InvalidRefreshTokenException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        return storedToken;
    }

    // 토큰으로 유저 찾기
    private UserSimpleInfoRes getUserByToken(String refreshToken) {
        String nickname = jwtTokenProvider.getNickname(refreshToken);
        DiscodeitUserDetails userDetails = (DiscodeitUserDetails) userDetailsService.loadUserByUsername(nickname);
        return userDetails.getUserInfo();
    }
}
