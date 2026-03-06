package domain.user.unit.service;

import com.sprint.mission.discodeit.domain.user.dto.request.UserFindPasswordReq;
import com.sprint.mission.discodeit.domain.user.dto.request.UserUpdateReq;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.exception.InvalidUserNicknameException;
import com.sprint.mission.discodeit.domain.user.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.domain.user.service.UserServiceImpl;
import com.sprint.mission.discodeit.global.email.EmailSender;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import domain.user.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailSender emailSender;

    @InjectMocks
    private UserServiceImpl userService;

    @Nested
    @DisplayName("닉네임을 이메일로 발송")
    class SendEmailNickname {
        @Test
        @DisplayName("성공: 존재하는 email이 들어올 경우 해당 이메일로 닉네임이 발송된다")
        void success_send_email_nickname() {
            // given
            User user = UserFixture.createWithoutProfile();

            given(userRepository.findByEmail(user.getEmail()))
                    .willReturn(Optional.of(user));

            // when
            userService.sendEmailId(user.getEmail());

            // then
            then(emailSender).should().sendEmailAsync(
                    eq(user.getEmail()),
                    anyString(),
                    contains(user.getNickname())
            );
        }

        @Test
        @DisplayName("실패: 존재하지 않는 email이 들어올 경우 예외가 발생한다")
        void fail_user_not_found() {
            // given
            String email = "";

            given(userRepository.findByEmail(email))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.sendEmailId(email))
                    .isInstanceOf(UserNotFoundException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_NOT_FOUND);
            then(emailSender).should(never()).sendEmailAsync(anyString(), anyString(), anyString());
        }
    }

    @Nested
    @DisplayName("메일로 임시 비밀번호 발송")
    class SendEmailTemporaryPassword {
        @Test
        @DisplayName("성공: 유효한 이메일과 닉네임이 들어올 경우 임시 비밀번호가 발급된다")
        void success_send_email_temporary_password() {
            // given
            User user = UserFixture.createWithoutProfile();
            UserFindPasswordReq req = new UserFindPasswordReq(user.getEmail(), user.getNickname());

            given(userRepository.findByEmail(user.getEmail()))
                    .willReturn(Optional.of(user));

            // when
            userService.sendEmailTemporaryPassword(req);

            // then
            then(emailSender).should().sendEmailAsync(
                    eq(user.getEmail()),
                    anyString(),
                    contains(user.getPassword())
            );
        }

        @Test
        @DisplayName("실패: 존재하지 않는 email이 들어올 경우 예외가 발생한다")
        void fail_user_not_found() {
            // given
            String email = "";

            given(userRepository.findByEmail(email))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.sendEmailId(email))
                    .isInstanceOf(UserNotFoundException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_NOT_FOUND);

            then(emailSender).should(never()).sendEmailAsync(anyString(), anyString(), anyString());
        }

        @Test
        @DisplayName("실패: 유저 닉네임과 일치하지 않는 nickname 이 들어올 경우 예외가 발생한다")
        void fail_user_invalid_nickname() {
            // given
            User user = UserFixture.createWithoutProfile();
            UserFindPasswordReq req = new UserFindPasswordReq(user.getEmail(), "invalid");
            given(userRepository.findByEmail(user.getEmail()))
                    .willReturn(Optional.of(user));

            // when & then
            assertThatThrownBy(() -> userService.sendEmailTemporaryPassword(req))
                    .isInstanceOf(InvalidUserNicknameException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_USER_NICKNAME);

            then(emailSender).should(never()).sendEmailAsync(anyString(), anyString(), anyString());
        }
    }

    @Nested
    @DisplayName("유저 생성")
    class CreateUser {
        @Test
        @DisplayName("성공: 유효한 User가 들어올 경우 유저가 생성된다")
        void success_create_user() {
            // given
            String email = "tester@aaa.com";
            String nickname = "tester";
            User user = User.createWithoutProfile(email, nickname, "qwer123$");

            given(userRepository.existsByEmail(email)).willReturn(false);
            given(userRepository.existsByNickname(nickname)).willReturn(false);
            given(userRepository.save(user)).willReturn(user);

            // when
            User result = userService.create(user);

            // then
            assertThat(result).isEqualTo(user);

            then(userRepository).should(times(1)).save(user);
        }

        @Test
        @DisplayName("실패: 이미 있는 이메일이 들어올 경우 예외가 발생한다")
        void fail_create_user_email_exist() {
            // given
            User existingUser = UserFixture.createWithoutProfile();
            String email = existingUser.getEmail();
            User newUser = User.createWithoutProfile(email, "tester", "qwer123$");

            given(userRepository.existsByEmail(email)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> userService.create(newUser))
                    .isInstanceOf(UserAlreadyExistsException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.EMAIL_ALREADY_EXISTS);

            then(userRepository).should(never()).save(any());
        }

        @Test
        @DisplayName("실패: 이미 있는 닉네임이 들어올 경우 예외가 발생한다")
        void fail_create_user_nickname_exist() {
            // given
            User existingUser = UserFixture.createWithoutProfile();
            String nickname = existingUser.getNickname();
            User newUser = User.createWithoutProfile("new@aaa.com", nickname, "qwer123$");

            given(userRepository.existsByEmail(newUser.getEmail())).willReturn(false);
            given(userRepository.existsByNickname(nickname)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> userService.create(newUser))
                    .isInstanceOf(UserAlreadyExistsException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.NICKNAME_ALREADY_EXISTS);

            then(userRepository).should(never()).save(any());
        }
    }

    @Nested
    @DisplayName("유저 목록 조회")
    class FindAllUsers {
        @Test
        @DisplayName("성공: 유저 목록이 조회된다")
        void success_find_all_users() {
            // given
            List<User> users = UserFixture.createMixedList();
            given(userRepository.findAll()).willReturn(users);

            // when
            List<User> result = userService.findAll();

            // then
            assertThat(result).isEqualTo(users);
            then(userRepository).should(times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("유저 id로 조회")
    class FindUserById {
        @Test
        @DisplayName("성공: 유효한 id가 들어올 경우 유저가 조회된다")
        void success_find_user() {
            // given
            User user = UserFixture.createWithoutProfile();
            given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

            // when
            User result = userService.findById(user.getId());

            // then
            assertThat(result).isEqualTo(user);
            then(userRepository).should(times(1)).findById(user.getId());
        }

        @Test
        @DisplayName("실패: 존재하지 않는 id가 들어올 경우 예외가 발생한다")
        void fail_user_not_found() {
            // given
            given(userRepository.findById(any(UUID.class)))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.findById(UUID.randomUUID()))
                    .isInstanceOf(UserNotFoundException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("유저 이메일로 조회")
    class FindUserByEmail {
        @Test
        @DisplayName("성공: 유효한 email이 들어올 경우 유저가 조회된다")
        void success_find_user() {
            // given
            User user = UserFixture.createWithoutProfile();
            given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

            // when
            User result = userService.findByEmail(user.getEmail());

            // then
            assertThat(result).isEqualTo(user);
            then(userRepository).should(times(1)).findByEmail(user.getEmail());
        }

        @Test
        @DisplayName("실패: 존재하지 않는 email이 들어올 경우 예외가 발생한다")
        void fail_user_not_found() {
            // given
            given(userRepository.findByEmail(any(String.class)))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.findByEmail("anything"))
                    .isInstanceOf(UserNotFoundException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("유저 닉네임으로 조회")
    class FindUserByNickname {
        @Test
        @DisplayName("성공: 유효한 nickname이 들어올 경우 유저가 조회된다")
        void success_find_user() {
            // given
            User user = UserFixture.createWithoutProfile();
            given(userRepository.findByNickname(user.getNickname())).willReturn(Optional.of(user));

            // when
            User result = userService.findByNickname(user.getNickname());

            // then
            assertThat(result).isEqualTo(user);
            then(userRepository).should(times(1)).findByNickname(user.getNickname());
        }
    }

    @Nested
    @DisplayName("유저 삭제")
    class DeleteUser {
        @Test
        @DisplayName("성공: 유효한 id가 들어올 경우 유저가 삭제된다")
        void success_delete_user() {
            // given
            User user = UserFixture.createWithoutProfile();

            // when
            userService.delete(user.getId());

            // then
            then(userRepository).should(times(1)).deleteById(user.getId());
        }
    }

    @Nested
    @DisplayName("유저 수정")
    class UpdateUser {
        @Test
        @DisplayName("성공: 유효한 id, email, nickname, password가 들어오면 유저 정보가 수정된다")
        void success_update_user() {
            // given
            User user = UserFixture.createWithoutProfile();
            UserUpdateReq req = new UserUpdateReq(
                    "new@aaa.com", "newNickname", "qwer1234$", null);
            given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

            // when
            userService.update(user.getId(), req);

            // then
            assertThat(user.getEmail()).isEqualTo(req.email());
            assertThat(user.getNickname()).isEqualTo(req.nickname());
            assertThat(user.getPassword()).isEqualTo(req.password());
        }

        @Test
        @DisplayName("성공: 유효한 id, email, nickname 이 들어오면 유저 정보는 수정되고, 비밀번호는 기존 것으로 유지된다")
        void success_update_user_password_not_change() {
            // given
            User user = UserFixture.createWithoutProfile();
            String originalPassword = user.getPassword();
            UserUpdateReq req = new UserUpdateReq(
                    "new@aaa.com", "newNickname", null, null);
            given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

            // when
            userService.update(user.getId(), req);

            // then
            assertThat(user.getEmail()).isEqualTo(req.email());
            assertThat(user.getNickname()).isEqualTo(req.nickname());
            assertThat(user.getPassword()).isEqualTo(originalPassword);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 id가 들어올 경우 예외가 발생한다")
        void fail_user_not_found() {
            // given
            UUID userId = UUID.randomUUID();
            UserUpdateReq req = new UserUpdateReq(
                    "test@test.com", "nickname", "password", null);
            given(userRepository.findById(userId))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.update(userId, req))
                    .isInstanceOf(UserNotFoundException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_NOT_FOUND);
        }

        @Test
        @DisplayName("실패: 이미 있는 이메일이 들어올 경우 예외가 발생한다")
        void fail_create_user_email_exist() {
            // given
            User existingUser = UserFixture.createWithoutProfile();
            String email = existingUser.getEmail();
            User user = User.createWithoutProfile("tester@aaa.com", "tester", "qwer123$");
            UserUpdateReq req = new UserUpdateReq(
                    email, "newNickname", "qwer1234$", null);

            given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
            given(userRepository.existsByIdNotAndEmail(user.getId(), email)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> userService.update(user.getId(), req))
                    .isInstanceOf(UserAlreadyExistsException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        @Test
        @DisplayName("실패: 이미 있는 닉네임이 들어올 경우 예외가 발생한다")
        void fail_create_user_nickname_exist() {
            // given
            User existingUser = UserFixture.createWithoutProfile();
            String nickname = existingUser.getNickname();
            User user = User.createWithoutProfile("tester@aaa.com", "tester", "qwer123$");
            UserUpdateReq req = new UserUpdateReq(
                    "tester@aaa.com", nickname, "qwer1234$", null);

            given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
            given(userRepository.existsByIdNotAndEmail(user.getId(), req.email())).willReturn(false);
            given(userRepository.existsByIdNotAndNickname(user.getId(), nickname)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> userService.update(user.getId(), req))
                    .isInstanceOf(UserAlreadyExistsException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }
    }

    @Nested
    @DisplayName("닉네임으로 가입 여부 확인")
    class CheckRegisteredNickname {
        @Test
        @DisplayName("성공: 닉네임이 들어올 경우 해당 닉네임으로 가입된 유저가 있는지 확인한다")
        void success_check_registered_nickname() {
            // given
            String nickname = "tester";
            given(userRepository.existsByNickname(nickname)).willReturn(false);

            // when
            userService.isRegisteredNickname(nickname);

            // then
            then(userRepository).should(times(1)).existsByNickname(nickname);
        }
    }

    @Nested
    @DisplayName("이메일로 가입 여부 확인")
    class CheckRegisteredEmail {
        @Test
        @DisplayName("성공: 이메일이 들어올 경우 해당 이메일로 가입된 유저가 있는지 확인한다")
        void success_check_registered_email() {
            // given
            String email = "tester@aaa.com";
            given(userRepository.existsByEmail(email)).willReturn(false);

            // when
            userService.isRegisteredEmail(email);

            // then
            then(userRepository).should(times(1)).existsByEmail(email);
        }
    }
}
