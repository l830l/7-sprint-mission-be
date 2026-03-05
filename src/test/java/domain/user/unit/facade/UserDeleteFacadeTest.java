package domain.user.unit.facade;

import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.facade.UserDeleteFacade;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserDeleteFacadeTest {
    @Mock
    BinaryContentService binaryContentService;

    @Mock
    BinaryContentStorage binaryContentStorage;

    @Mock
    private UserService userService;

    @Mock
    private UserStatusService userStatusService;

    @InjectMocks
    private UserDeleteFacade userDeleteFacade;

    @Nested
    @DisplayName("프로필, 상태 없는 유저 삭제")
    class DeleteUser {
        @Test
        @DisplayName("성공: 유효한 id가 들어올 경우 유저가 삭제된다")
        void success_delete_user() {
            // given
            User user = UserFixture.createWithoutProfile();
            given(userService.findById(user.getId())).willReturn(user);
            given(userStatusService.findByUserId(user.getId())).willReturn(null);

            // when
            userDeleteFacade.deleteUser(user.getId());

            // then
            then(binaryContentService).should(never()).delete(any());
            then(binaryContentStorage).should(never()).delete(any());
            then(userStatusService).should(never()).delete(any());
            then(userService).should(times(1)).delete(user.getId());
        }
    }

    @Nested
    @DisplayName("상태가 존재하는 유저 삭제")
    class DeleteWithStatusUser {
        @Test
        @DisplayName("성공: 유효한 id가 들어오면 status와 유저가 삭제된다")
        void success_delete_with_status_user() {
            // given
            User user = UserFixture.createWithoutProfile();
            UserStatus userStatus = UserStatus.create(user);

            given(userService.findById(user.getId())).willReturn(user);
            given(userStatusService.findByUserId(user.getId())).willReturn(userStatus);

            // when
            userDeleteFacade.deleteUser(user.getId());

            // then
            then(binaryContentService).should(never()).delete(any());
            then(binaryContentStorage).should(never()).delete(any());
            then(userStatusService).should(times(1)).delete(userStatus.getId());
            then(userService).should(times(1)).delete(user.getId());
        }
    }

    @Nested
    @DisplayName("상태와 프로필이 존재하는 유저 삭제")
    class DeleteWithStatusAndProfileUser {
        @Test
        @DisplayName("성공: 유효한 id가 들어오면 프로필과 status와 유저가 삭제된다")
        void success_delete_with_status_user() {
            // given
            User user = UserFixture.createWithProfile();
            UserStatus userStatus = UserStatus.create(user);

            given(userService.findById(user.getId())).willReturn(user);
            given(userStatusService.findByUserId(user.getId())).willReturn(userStatus);

            // when
            userDeleteFacade.deleteUser(user.getId());

            // then
            then(binaryContentService).should(times(1)).delete(user.getProfile().getId());
            then(binaryContentStorage).should(times(1)).delete(user.getProfile().getId());
            then(userStatusService).should(times(1)).delete(userStatus.getId());
            then(userService).should(times(1)).delete(user.getId());
        }
    }
}