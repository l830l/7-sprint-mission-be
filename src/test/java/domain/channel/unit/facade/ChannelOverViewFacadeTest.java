package domain.channel.unit.facade;

import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.facade.ChannelOverViewFacade;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import com.sprint.mission.discodeit.domain.channelmember.service.ChannelMemberService;
import com.sprint.mission.discodeit.domain.user.entity.User;
import domain.channel.fixture.ChannelFixture;
import domain.user.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
        @Test
        @DisplayName("성공: 유효한 userId가 들어올 경우, 해당 유저가 참여한 채널 목록이 조회된다")
        void success_overview() {
            // given
            User user = UserFixture.createWithoutProfile();
            List<Channel> channels = ChannelFixture.createChannelList(user);

            //
        }

        @Test
        @DisplayName("""
                    성공: 유효한 userId와 검색어가 들어올 경우,
                    해당 유저가 참여한 채널 목록 중에서
                    검색어가 포함된 채널들만 조회된다
                """)
        void success_overview_with_search() {
        }

        @Test
        @DisplayName("실패: 채널 중 일반적이지 않은 타입이 존재하면 예외가 발생한다")
        void fail_overview_with_invalid_channel_type() {
        }
    }
}
