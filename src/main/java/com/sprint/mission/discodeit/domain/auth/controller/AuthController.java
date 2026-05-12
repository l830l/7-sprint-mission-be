package com.sprint.mission.discodeit.domain.auth.controller;

import com.sprint.mission.discodeit.domain.auth.controller.docs.AuthControllerDocs;
import com.sprint.mission.discodeit.domain.auth.cookie.RefreshTokenCookieProvider;
import com.sprint.mission.discodeit.domain.auth.dto.inner.JwtRefreshResult;
import com.sprint.mission.discodeit.domain.auth.dto.request.EmailCheckReq;
import com.sprint.mission.discodeit.domain.auth.dto.request.UserLoginReq;
import com.sprint.mission.discodeit.domain.auth.dto.request.UserRoleUpdateReq;
import com.sprint.mission.discodeit.domain.auth.dto.request.VerifyCodeReq;
import com.sprint.mission.discodeit.domain.auth.dto.response.JwtRes;
import com.sprint.mission.discodeit.domain.auth.dto.response.VerifyCodeRes;
import com.sprint.mission.discodeit.domain.auth.facade.AuthFacade;
import com.sprint.mission.discodeit.domain.auth.service.AuthRefreshService;
import com.sprint.mission.discodeit.domain.auth.service.AuthServiceImpl;
import com.sprint.mission.discodeit.domain.user.dto.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.domain.user.dto.response.UserSimpleInfoRes;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

    private final AuthFacade authFacade;
    private final AuthServiceImpl basicAuthService;
    private final UserService userService;
    private final RefreshTokenCookieProvider refreshTokenCookieProvider;
    private final AuthRefreshService authRefreshService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserDetailInfoRes> login(@Valid @RequestBody UserLoginReq req) {
        return ResponseEntity.ok(authFacade.login(req));
    }

    //이메일 인증 코드 발송
    @PostMapping("/email-code")
    public ResponseEntity<Void> sendEmailAuthCode(@Valid @RequestBody EmailCheckReq req) {
        String email = req.email();
        basicAuthService.sendEmailCode(email);
        return ResponseEntity.noContent().build();
    }

    //이메일 인증 결과 확인
    @PostMapping("/email-code/verify")
    public ResponseEntity<VerifyCodeRes> verifyEmailAuthCode(@Valid @RequestBody VerifyCodeReq req) {
        return ResponseEntity.ok(basicAuthService.verifyEmailCode(req.email(), req.code()));
    }

    // csrf 토큰 발급
    @GetMapping("/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
        csrfToken.getToken();
        return ResponseEntity.noContent().build();
    }

    // 유저 권한 수정
    @PatchMapping("/role")
    public ResponseEntity<UserSimpleInfoRes> updateUserRole(
            @Valid @RequestBody UserRoleUpdateReq req
    ) {
        return ResponseEntity.ok(userService.updateRole(req.userId(), req.userRole()));
    }

    // 리프레쉬 토큰
    @PostMapping("/refresh")
    public ResponseEntity<JwtRes> refreshToken(
            HttpServletRequest request, HttpServletResponse response
    ) {
        String refreshToken = refreshTokenCookieProvider.extractToken(request);
        JwtRefreshResult result = authRefreshService.refresh(refreshToken);
        refreshTokenCookieProvider.addRefreshCookie(response, result.refreshToken());
        return ResponseEntity.ok(new JwtRes(result.userInfo(), result.accessToken()));
    }
}
