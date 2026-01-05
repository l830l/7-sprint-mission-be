package com.sprint.mission.discodeit.facade.auth;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sprint.mission.discodeit.dto.auth.request.UserLoginReq;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("인증 Facade 단위 테스트")
class AuthFacadeTest {

  @Mock
  private UserService userService;

  @Mock
  private UserStatusService userStatusService;

  @Mock
  private BinaryContentService binaryContentService;

  @InjectMocks
  private AuthFacade authFacade;

  @Nested
  @DisplayName("로그인")
  class LoginTest {

    @Test
    @DisplayName("닉네임이 존재하지 않으면 INVALID_NICKNAME 예외 발생")
    void login_fail_invalidNickname() {
      //given
      UserLoginReq req = new UserLoginReq("nickname", "password");
      when(userService.findByNickname(req.nickname()))
          .thenReturn(null);

      // when & then
      DiscodeitException exception = catchThrowableOfType(
          () -> authFacade.login(req),
          DiscodeitException.class
      );

      assertThat(exception.getErrorCode())
          .isEqualTo(ErrorCode.INVALID_NICKNAME);
    }


  }
}