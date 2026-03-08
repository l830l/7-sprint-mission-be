package domain.channelmember.fixture;

import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channelmember.entity.ChannelMember;
import com.sprint.mission.discodeit.domain.channelmember.entity.ChannelMemberRole;
import com.sprint.mission.discodeit.domain.user.entity.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

public class ChannelMemberFixture {
    public static ChannelMember create(User user, Channel channel) {
        ChannelMember channelMember = ChannelMember.create(
                user, channel, ChannelMemberRole.MEMBER);
        ReflectionTestUtils.setField(channelMember, "id", UUID.randomUUID());
        return channelMember;
    }
}
