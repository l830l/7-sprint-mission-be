package com.sprint.mission.discodeit.domain.channel.unit.facade;

import com.sprint.mission.discodeit.domain.channel.dto.request.ChannelUpdateReq;
import com.sprint.mission.discodeit.domain.channel.dto.response.ChannelInfoRes;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.facade.ChannelUpdateFacade;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.channel.fixture.ChannelFixture;
import com.sprint.mission.discodeit.domain.user.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ChannelUpdateFacadeTest {
    @Mock
    private ChannelService channelService;

    @InjectMocks
    private ChannelUpdateFacade channelUpdateFacade;

    @Nested
    @DisplayName("성공: 공개 채널 수정")
    class UpdateChannel {
        @Test
        @DisplayName("성공: 유효한 id, name, description 이 들어올 경우 정보가 수정된다")
        void success_update_channel() {
            // given
            Channel channel = ChannelFixture.createPublicChannel();
            User manager = UserFixture.createWithoutProfile();
            ChannelUpdateReq req = new ChannelUpdateReq(
                    "새 채널",
                    "이것은 새 채널입니다."
            );
            given(channelService.get(channel.getId()))
                    .willReturn(ChannelFixture.toInfoQuery(channel, manager.getId()));

            // when
            ChannelInfoRes result = channelUpdateFacade.update(channel.getId(), req);

            // then
            assertThat(result).isNotNull();
            then(channelService).should(times(1)).update(channel.getId(), req);
            then(channelService).should(times(1)).get(channel.getId());
        }
    }
}
