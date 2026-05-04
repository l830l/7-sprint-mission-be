package com.sprint.mission.discodeit.domain.user.unit.facade;

import com.sprint.mission.discodeit.domain.user.dto.response.UserSimpleInfoRes;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.facade.UserOverviewFacade;
import com.sprint.mission.discodeit.domain.user.fixture.UserFixture;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import com.sprint.mission.discodeit.domain.user.service.UserSessionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserOverviewFacadeTest {
    @Mock
    private UserService userService;

    @Mock
    private UserSessionService userSessionService;

    @InjectMocks
    private UserOverviewFacade userOverviewFacade;

    @Nested
    @DisplayName("유저 목록 조회")
    class FindAllUsers {
        @Test
        @DisplayName("성공: 유저 전체 목록이 조회된다")
        void success_find_all_users() {
            // given
            List<User> users = UserFixture.createMixedList();
            given(userService.findAll()).willReturn(users);
            given(userSessionService.isOnline(any(UUID.class))).willReturn(true);

            // when
            List<UserSimpleInfoRes> result = userOverviewFacade.findAll();

            // then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(users.size());

            then(userService).should(times(1)).findAll();
        }
    }
}
