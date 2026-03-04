package domain.user.unit.facade;

import com.sprint.mission.discodeit.domain.user.dto.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.facade.UserDetailViewFacade;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import com.sprint.mission.discodeit.domain.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.domain.userstatus.service.UserStatusService;
import domain.user.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserDetailViewFacadeTest {
    @Mock
    private UserService userService;

    @Mock
    private UserStatusService userStatusService;

    @InjectMocks
    private UserDetailViewFacade userDetailViewFacade;

    @Nested
    @DisplayName("유저 id로 조회")
    class FindByIdUser {
        @Test
        @DisplayName("성공: 유효한 id가 들어올 경우 유저가 조회된다")
        void success_find_by_id_user() {
            // given
            User user = UserFixture.createWithoutProfile();
            UserStatus userStatus = UserStatus.create(user);
            given(userStatusService.findByUserId(user.getId())).willReturn(userStatus);
            given(userService.findById(any(UUID.class))).willReturn(user);

            // when
            UserDetailInfoRes result = userDetailViewFacade.findById(user.getId());

            // then
            assertThat(result).isNotNull();

            then(userStatusService).should(times(1)).findByUserId(user.getId());
            then(userService).should(times(1)).findById(user.getId());
        }
    }

    @Nested
    @DisplayName("유저 닉네임으로 조회")
    class FindByNicknameUser {
        @Test
        @DisplayName("성공: 유요한 닉네임이 들어올 경우 유저가 조회된다")
        void success_find_by_nickname_user() {
            // given
            User user = UserFixture.createWithoutProfile();
            UserStatus userStatus = UserStatus.create(user);
            given(userStatusService.findByUserId(user.getId())).willReturn(userStatus);
            given(userService.findByNickname(any(String.class))).willReturn(user);

            // when
            UserDetailInfoRes result = userDetailViewFacade.findByNickname(user.getNickname());

            // then
            assertThat(result).isNotNull();

            then(userStatusService).should(times(1)).findByUserId(user.getId());
            then(userService).should(times(1)).findByNickname(user.getNickname());
        }
    }

    @Nested
    @DisplayName("유저 이메일로 조회")
    class FindByEmailUser {
        @Test
        @DisplayName("성공: 유효한 이메일이 들어올 경우 유저가 조회된다")
        void success_find_by_email_user() {
            // given
            User user = UserFixture.createWithoutProfile();
            UserStatus userStatus = UserStatus.create(user);
            given(userStatusService.findByUserId(user.getId())).willReturn(userStatus);
            given(userService.findByEmail(any(String.class))).willReturn(user);

            // when
            UserDetailInfoRes result = userDetailViewFacade.findByEmail(user.getEmail());

            // then
            assertThat(result).isNotNull();

            then(userStatusService).should(times(1)).findByUserId(user.getId());
            then(userService).should(times(1)).findByEmail(user.getEmail());
        }
    }
}
