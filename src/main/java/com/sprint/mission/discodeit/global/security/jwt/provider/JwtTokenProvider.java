package com.sprint.mission.discodeit.global.security.jwt.provider;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sprint.mission.discodeit.domain.user.dto.response.UserSimpleInfoRes;
import com.sprint.mission.discodeit.domain.user.entity.UserRole;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.properties.JwtProperties;
import com.sprint.mission.discodeit.global.security.exception.JwtClaimExtractFailException;
import com.sprint.mission.discodeit.global.security.exception.JwtCreateFailException;
import com.sprint.mission.discodeit.global.security.jwt.constant.JwtClaim;
import com.sprint.mission.discodeit.global.security.jwt.constant.JwtTokenType;
import com.sprint.mission.discodeit.global.security.jwt.util.TokenHashUtils;
import com.sprint.mission.discodeit.global.security.jwt.vo.IssuedJwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;

    // [토큰 발급]
    public IssuedJwtToken issueToken(UserSimpleInfoRes userInfo) {
        String accessToken = createAccessToken(userInfo);
        String refreshToken = createRefreshToken(userInfo);

        return new IssuedJwtToken(
                accessToken,
                refreshToken,
                TokenHashUtils.sha256(accessToken),
                TokenHashUtils.sha256(refreshToken),
                createAccessTokenExpiresAt(),
                createRefreshTokenExpiresAt()
        );
    }

    public Instant createRefreshTokenExpiresAt() {
        return Instant.now().plusSeconds(jwtProperties.refreshTokenExpirationSeconds());
    }

    public Instant createAccessTokenExpiresAt() {
        return Instant.now().plusSeconds(jwtProperties.accessTokenExpirationSeconds());
    }

    // [토큰 생성]
    public String createAccessToken(UserSimpleInfoRes userInfo) {
        return createToken(
                userInfo,
                JwtTokenType.ACCESS,
                jwtProperties.accessTokenExpirationSeconds()
        );
    }

    public String createRefreshToken(UserSimpleInfoRes userInfo) {
        return createToken(
                userInfo,
                JwtTokenType.REFRESH,
                jwtProperties.refreshTokenExpirationSeconds()
        );
    }

    private String createToken(
            UserSimpleInfoRes userInfo,
            JwtTokenType tokenType,
            long expirationSeconds
    ) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(expirationSeconds);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userInfo.userId().toString())
                .issueTime(Date.from(now))
                .expirationTime(Date.from(expiration))
                .claim(JwtClaim.USER_ID.getValue(), userInfo.userId().toString())
                .claim(JwtClaim.NICKNAME.getValue(), userInfo.nickname())
                .claim(JwtClaim.EMAIL.getValue(), userInfo.email())
                .claim(JwtClaim.ROLE.getValue(), userInfo.userRole().name())
                .claim(JwtClaim.TOKEN_TYPE.getValue(), tokenType.name())
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claimsSet
        );

        try {
            signedJWT.sign(new MACSigner(jwtProperties.secret()));
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new JwtCreateFailException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // [토큰 검증]
    // 액세스 토큰이 맞는지 확인
    public boolean validateAccessToken(String token) {
        return validateToken(token)
                && hasTokenType(token, JwtTokenType.ACCESS);
    }

    // 리프레쉬 토큰이 맞는지 확인
    public boolean validateRefreshToken(String token) {
        return validateToken(token)
                && hasTokenType(token, JwtTokenType.REFRESH);
    }

    // 토큰 타입이 맞는지 확인
    private boolean hasTokenType(String token, JwtTokenType tokenType) {
        try {
            String tokenTypeClaim = getClaim(token, JwtClaim.TOKEN_TYPE);
            return tokenType.name().equals(tokenTypeClaim);
        } catch (Exception e) {
            return false;
        }
    }

    // 기본 토큰 검증
    private boolean validateToken(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            if (!jwt.verify(new MACVerifier(jwtProperties.secret()))) {
                return false;
            }
            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
            return isNotExpired(claimsSet) && hasRequiredClaims(claimsSet);
        } catch (ParseException | JOSEException e) {
            return false;
        }
    }

    // 유효시간 만료 확인
    private boolean isNotExpired(JWTClaimsSet claims) {
        Date expiration = claims.getExpirationTime();
        return expiration != null && expiration.after(new Date());
    }

    // Claims 가 null 인지 확인
    private boolean hasRequiredClaims(JWTClaimsSet claims) throws ParseException {
        return claims.getSubject() != null
                && claims.getStringClaim(JwtClaim.USER_ID.getValue()) != null
                && claims.getStringClaim(JwtClaim.ROLE.getValue()) != null
                && claims.getStringClaim(JwtClaim.TOKEN_TYPE.getValue()) != null;
    }

    // [Claim 추출]
    public UUID getUserId(String token) {
        return UUID.fromString(getClaim(token, JwtClaim.USER_ID));
    }

    public String getNickname(String token) {
        return getClaim(token, JwtClaim.NICKNAME);
    }

    public String getEmail(String token) {
        return getClaim(token, JwtClaim.EMAIL);
    }

    public UserRole getUserRole(String token) {
        return UserRole.valueOf(getClaim(token, JwtClaim.ROLE));
    }


    public JwtTokenType getTokenType(String token) {
        return JwtTokenType.valueOf(getClaim(token, JwtClaim.TOKEN_TYPE));
    }

    // Json 형태에서 특정 클레임을 꺼내기
    private String getClaim(String token, JwtClaim claim) {
        try {
            return SignedJWT.parse(token)
                    .getJWTClaimsSet()
                    .getStringClaim(claim.getValue());
        } catch (ParseException e) {
            throw new JwtClaimExtractFailException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
