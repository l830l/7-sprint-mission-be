package com.sprint.mission.discodeit.domain.user.unit.facade;

import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.user.dto.request.UserCreateReq;
import com.sprint.mission.discodeit.domain.user.dto.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.facade.UserCreationFacade;
import com.sprint.mission.discodeit.domain.user.factory.UserFactory;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import com.sprint.mission.discodeit.domain.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.domain.userstatus.service.UserStatusService;
import com.sprint.mission.discodeit.domain.binarycontent.fixture.BinaryContentFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserCreationFacadeTest {
    @Mock
    private UserService userService;

    @Mock
    private BinaryContentService binaryContentService;

    @Mock
    private BinaryContentStorage binaryContentStorage;

    @Mock
    private UserStatusService userStatusService;

    @Mock
    private UserFactory userFactory;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserCreationFacade userCreationFacade;

    @Nested
    @DisplayName("프로필 이미지가 없는 유저 생성")
    class CreateUserWithoutProfile {
        @Test
        @DisplayName("성공: 이메일, 닉네임, 비밀번호가 정상적으로 들어오면 유저가 생성된다")
        void success_create_user_without_profile() {
            // given
            String email = "tester@aaa.com";
            String nickname = "tester";
            String password = "qwer123$";
            String passwordEncoded = "qwer123$encoded";
            UserCreateReq req = new UserCreateReq(email, nickname, password, null);
            User user = User.createWithoutProfile(email, nickname, passwordEncoded);
            UserStatus userStatus = UserStatus.create(user);

            given(userFactory.create(req, passwordEncoded, null)).willReturn(user);
            given(userService.create(any(User.class)))
                    .willReturn(user);
            given(userStatusService.create(any(UserStatus.class)))
                    .willReturn(userStatus);
            given(passwordEncoder.encode(password))
                    .willReturn(passwordEncoded);

            // when
            UserDetailInfoRes res = userCreationFacade.createUser(req);

            // then
            assertThat(res).isNotNull();

            then(binaryContentService).should(never()).upload(any());
            then(binaryContentStorage).should(never()).put(any(), any());
            then(userFactory).should().create(eq(req), anyString(), eq(null));
            then(userService).should().create(any(User.class));
            then(userStatusService).should().create(any(UserStatus.class));
        }
    }

    @Nested
    @DisplayName("성공: 프로필 이미지가 있는 유저 생성")
    class CreateUserWithProfile {
        @Test
        @DisplayName("성공: 성공: 이메일, 닉네임, 비밀번호, 이미지가 정상적으로 들어오면 유저가 생성된다")
        void success_create_user_with_profile() {
            // given
            String email = "tester@aaa.com";
            String nickname = "tester";
            String password = "qwer123$";
            String passwordEncoded = "qwer123$encoded";
            BinaryContentCreateReq binaryReq = new BinaryContentCreateReq(
                    "test_profile".getBytes(), "test_profile.jpg", "image/jpg", 10L);
            BinaryContent binaryContent = BinaryContentFixture.create(binaryReq);
            UserCreateReq req = new UserCreateReq(email, nickname, password, binaryReq);
            User user = User.createWithProfile(req.email(), req.nickname(), passwordEncoded, binaryContent);
            UserStatus userStatus = UserStatus.create(user);

            given(binaryContentService.upload(binaryReq)).willReturn(binaryContent);
            given(userFactory.create(req, passwordEncoded, binaryContent.getId())).willReturn(user);
            given(userService.create(any(User.class))).willReturn(user);
            given(binaryContentStorage.put(binaryContent.getId(), req.profileImage().data()))
                    .willReturn(binaryContent.getId());
            given(userStatusService.create(any(UserStatus.class))).willReturn(userStatus);
            given(passwordEncoder.encode(password))
                    .willReturn(passwordEncoded);

            // when
            UserDetailInfoRes res = userCreationFacade.createUser(req);

            // then
            assertThat(res).isNotNull();

            then(binaryContentService).should(times(1)).upload(binaryReq);
            then(binaryContentStorage).should(times(1)).put(binaryContent.getId(), req.profileImage().data());
            then(userFactory).should().create(req, passwordEncoded, binaryContent.getId());
            then(userService).should().create(any(User.class));
            then(userStatusService).should().create(any(UserStatus.class));
        }
    }
}
