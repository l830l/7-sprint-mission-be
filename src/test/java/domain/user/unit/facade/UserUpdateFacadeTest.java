package domain.user.unit.facade;

import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.user.dto.request.UserUpdateReq;
import com.sprint.mission.discodeit.domain.user.dto.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.facade.UserUpdateFacade;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import com.sprint.mission.discodeit.domain.userstatus.service.UserStatusService;
import domain.binarycontent.fixture.BinaryContentFixture;
import domain.user.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserUpdateFacadeTest {
    @Mock
    BinaryContentService binaryContentService;

    @Mock
    BinaryContentStorage binaryContentStorage;

    @Mock
    private UserService userService;

    @Mock
    private UserStatusService userStatusService;

    @InjectMocks
    private UserUpdateFacade userUpdateFacade;

    @Nested
    @DisplayName("프로필 없는 유저 수정")
    class UpdateWithOutProfileUser {
        @Test
        @DisplayName("성공: 프로필 없이 정보 수정을 할 경우 프로필 없는 새로운 유저 정보가 반영된다")
        void success_update_without_profile_user() {
            // given
            User user = UserFixture.createWithoutProfile();
            UserUpdateReq req = new UserUpdateReq(
                    "changed@aaa.com",
                    "changedName",
                    "changedPassword",
                    null
            );

            given(userService.findById(user.getId())).willReturn(user);
            doAnswer(invocation -> {
                user.update(req.email(), req.nickname(), req.password());
                return null;
            }).when(userService).update(user.getId(), req);

            // when
            UserDetailInfoRes result = userUpdateFacade.updateUser(user.getId(), req);

            // then
            assertThat(result).isNotNull();
            assertThat(result.email()).isEqualTo(req.email());
            assertThat(result.nickname()).isEqualTo(req.nickname());
            assertThat(result.profileImg()).isNull();
            assertThat(result.updateAt()).isEqualTo(user.getUpdatedAt());

            then(userService).should().update(user.getId(), req);
            then(binaryContentService).should(never()).delete(any());
            then(binaryContentStorage).should(never()).delete(any());
            then(binaryContentService).should(never()).upload(any());
            then(binaryContentStorage).should(never()).put(any(), any());
            then(userStatusService).should().updateByUserId(user.getId());
        }

        @Test
        @DisplayName("성공: 프로필 업로드와 정보 수정을 할 경우 새로운 프로필과 변경된 유저 정보가 반영된다")
        void success_update_with_profile_user() {
            // given
            User user = UserFixture.createWithoutProfile();
            BinaryContentCreateReq binaryReq = new BinaryContentCreateReq(
                    "test".getBytes(),
                    "test_file",
                    "image/png",
                    30L
            );
            BinaryContent binaryContent = BinaryContentFixture.create(binaryReq);
            UserUpdateReq req = new UserUpdateReq(
                    "changed@aaa.com",
                    "changedName",
                    "changedPassword",
                    binaryReq
            );

            given(userService.findById(user.getId())).willReturn(user);
            given(binaryContentService.upload(binaryReq))
                    .willReturn(binaryContent);

            doAnswer(invocation -> {
                user.update(req.email(), req.nickname(), req.password());
                return null;
            }).when(userService).update(user.getId(), req);


            // when
            UserDetailInfoRes result = userUpdateFacade.updateUser(user.getId(), req);

            // then
            assertThat(result).isNotNull();
            assertThat(result.email()).isEqualTo(req.email());
            assertThat(result.nickname()).isEqualTo(req.nickname());
            assertThat(result.profileImg()).isNotNull();
            assertThat(result.profileImg().binaryContentId()).isEqualTo(binaryContent.getId());
            assertThat(result.profileImg().fileName()).isEqualTo(binaryReq.fileName());
            assertThat(result.profileImg().fileType()).isEqualTo(binaryReq.fileType());
            assertThat(result.profileImg().fileSize()).isEqualTo(binaryReq.fileSize());
            assertThat(result.updateAt()).isEqualTo(user.getUpdatedAt());

            then(userService).should().update(user.getId(), req);
            then(binaryContentService).should(never()).delete(any());
            then(binaryContentStorage).should(never()).delete(any());
            then(binaryContentService).should(times(1)).upload(binaryReq);
            then(binaryContentStorage).should(times(1))
                    .put(binaryContent.getId(), binaryReq.data());
            then(userStatusService).should().updateByUserId(user.getId());
        }
    }

    @Nested
    @DisplayName("프로필 있는 유저 수정")
    class UpdateWithProfileUser {
        @Test
        @DisplayName("성공: 프로필 없이 정보 수정을 할 경우 프로필 없는 새로운 유저 정보가 반영된다")
        void success_update_without_profile_user() {
            // given
            User user = UserFixture.createWithProfile();
            UserUpdateReq req = new UserUpdateReq(
                    "changed@aaa.com",
                    "changedName",
                    "changedPassword",
                    null
            );

            given(userService.findById(user.getId())).willReturn(user);
            doAnswer(invocation -> {
                user.update(req.email(), req.nickname(), req.password());
                return null;
            }).when(userService).update(user.getId(), req);

            // when
            UserDetailInfoRes result = userUpdateFacade.updateUser(user.getId(), req);

            // then
            assertThat(result).isNotNull();
            assertThat(result.email()).isEqualTo(req.email());
            assertThat(result.nickname()).isEqualTo(req.nickname());
            assertThat(result.profileImg()).isNull();
            assertThat(result.updateAt()).isEqualTo(user.getUpdatedAt());

            then(userService).should().update(user.getId(), req);
            then(binaryContentService).should(times(1)).delete(any());
            then(binaryContentStorage).should(times(1)).delete(any());
            then(binaryContentService).should(never()).upload(any());
            then(binaryContentStorage).should(never()).put(any(), any());
            then(userStatusService).should().updateByUserId(user.getId());
        }

        @Test
        @DisplayName("성공: 프로필 업로드와 정보 수정을 할 경우 새로운 프로필과 변경된 유저 정보가 반영된다")
        void success_update_with_profile_user() {
            // given
            User user = UserFixture.createWithProfile();
            BinaryContentCreateReq binaryReq = new BinaryContentCreateReq(
                    "test".getBytes(),
                    "test_file",
                    "image/png",
                    30L
            );
            BinaryContent binaryContent = BinaryContentFixture.create(binaryReq);
            UserUpdateReq req = new UserUpdateReq(
                    "changed@aaa.com",
                    "changedName",
                    "changedPassword",
                    binaryReq
            );

            given(userService.findById(user.getId())).willReturn(user);
            given(binaryContentService.upload(binaryReq))
                    .willReturn(binaryContent);

            doAnswer(invocation -> {
                user.update(req.email(), req.nickname(), req.password());
                return null;
            }).when(userService).update(user.getId(), req);


            // when
            UserDetailInfoRes result = userUpdateFacade.updateUser(user.getId(), req);

            // then
            assertThat(result).isNotNull();
            assertThat(result.email()).isEqualTo(req.email());
            assertThat(result.nickname()).isEqualTo(req.nickname());
            assertThat(result.profileImg()).isNotNull();
            assertThat(result.profileImg().binaryContentId()).isEqualTo(binaryContent.getId());
            assertThat(result.profileImg().fileName()).isEqualTo(binaryReq.fileName());
            assertThat(result.profileImg().fileType()).isEqualTo(binaryReq.fileType());
            assertThat(result.profileImg().fileSize()).isEqualTo(binaryReq.fileSize());
            assertThat(result.updateAt()).isEqualTo(user.getUpdatedAt());

            then(userService).should().update(user.getId(), req);
            then(binaryContentService).should(times(1)).delete(any());
            then(binaryContentStorage).should(times(1)).delete(any());
            then(binaryContentService).should(times(1)).upload(binaryReq);
            then(binaryContentStorage).should(times(1))
                    .put(binaryContent.getId(), binaryReq.data());
            then(userStatusService).should().updateByUserId(user.getId());
        }
    }
}
