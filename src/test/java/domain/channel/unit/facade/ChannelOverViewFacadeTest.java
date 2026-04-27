package domain.channel.unit.facade;

import com.sprint.mission.discodeit.domain.channel.dto.query.ChannelInfoQuery;
import com.sprint.mission.discodeit.domain.channel.dto.response.ChannelInfoRes;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;
import com.sprint.mission.discodeit.domain.channel.facade.ChannelOverViewFacade;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import com.sprint.mission.discodeit.domain.channelmember.service.ChannelMemberService;
import com.sprint.mission.discodeit.domain.user.entity.User;
import domain.channel.fixture.ChannelFixture;
import domain.channelmember.fixture.ChannelMemberFixture;
import domain.user.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ChannelOverViewFacadeTest {
    @Mock
    private ChannelMemberService channelMemberService;

    @Mock
    private ChannelService channelService;

    @InjectMocks
    private ChannelOverViewFacade channelOverViewFacade;

    @Nested
    @DisplayName("채널 목록 조회")
    class FindAllChannels {
        @ParameterizedTest
        @ValueSource(strings = {
                "",
                "   ",
                "test"
        })
        @DisplayName("성공: 채널 목록을 조회하고, 비밀방마다 채널 멤ㅂ들을 조회한다.")
        void success_overview(String searchTxt) {
            // given
            User manager = UserFixture.createWithoutProfile();
            User member = UserFixture.createWithoutProfile();

            List<Channel> channelList = List.of(
                    ChannelFixture.createPrivateChannel(),
                    ChannelFixture.createPublicChannel(),
                    ChannelFixture.createPrivateChannel(),
                    ChannelFixture.createPublicChannel(),
                    ChannelFixture.createPrivateChannel()
            );
            int privateCount = (int) channelList.stream()
                    .filter(channel -> channel.getPublicType() == ChannelType.PRIVATE)
                    .count();
            List<ChannelInfoQuery> channelInfoQueryList = channelList.stream().map(
                    channel -> new ChannelInfoQuery(
                            channel.getId(),
                            channel.getName(),
                            channel.getDescription(),
                            channel.getPublicType(),
                            manager.getId(),
                            null
                    )
            ).toList();
            String normalizedSearch =
                    searchTxt == null || searchTxt.trim().isEmpty() ? "" : searchTxt;

            given(channelService.getAllByUser(member.getId(), normalizedSearch))
                    .willReturn(channelInfoQueryList);
            given(channelMemberService.findAllByChannelId(any(UUID.class)))
                    .willAnswer(invocation -> {
                        UUID channelId = invocation.getArgument(0);
                        Channel currentChannel = channelList.stream()
                                .filter(channel -> channel.getId().equals(channelId))
                                .findFirst()
                                .orElseThrow();
                        return List.of(
                                ChannelMemberFixture.create(currentChannel, manager),
                                ChannelMemberFixture.create(currentChannel, member)
                        );
                    });

            // when
            Map<ChannelType, List<ChannelInfoRes>> result = channelOverViewFacade.findAllMyChannels(
                    member.getId(),
                    searchTxt
            );

            // then
            then(channelService).should(times(1))
                    .getAllByUser(member.getId(), normalizedSearch);
            then(channelMemberService).should(times(privateCount))
                    .findAllByChannelId(any(UUID.class));

            assertThat(result).isNotNull();
            assertThat(result.get(ChannelType.PUBLIC)).hasSize(channelList.size() - privateCount);
            assertThat(result.get(ChannelType.PRIVATE)).hasSize(privateCount);
        }
    }
}
