package domain.channel.fixture;

import com.sprint.mission.discodeit.domain.channel.dto.query.ChannelInfoQuery;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.user.entity.User;
import domain.channelmember.fixture.ChannelMemberFixture;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChannelFixture {
    private static int channelCount = 1;

    // 공개 채널 생성
    public static Channel createPublicChannel() {
        Channel newChannel = Channel.createPublic(
                "channel" + channelCount,
                "설명" + channelCount);
        ReflectionTestUtils.setField(newChannel, "id", UUID.randomUUID());
        channelCount++;
        return newChannel;
    }

    public static Channel createPublicChannel(String name, String description) {
        Channel newChannel = Channel.createPublic(name, description);
        ReflectionTestUtils.setField(newChannel, "id", UUID.randomUUID());
        channelCount++;
        return newChannel;
    }

    // 비밀 채널 생성
    public static Channel createPrivateChannel() {
        Channel newChannel = Channel.createPrivate();
        ReflectionTestUtils.setField(newChannel, "id", UUID.randomUUID());
        return newChannel;
    }

    // Channel -> ChannelInfoQuery
    public static ChannelInfoQuery toInfoQuery(Channel channel, UUID managerId) {
        return new ChannelInfoQuery(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getPublicType(),
                managerId,
                null
        );
    }

    // 유저가 속한 비밀 채널들과 공개채널 생성
    public static List<Channel> createChannelList(User user) {
        List<Channel> channels = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            channels.add(createPublicChannel());
            Channel privateChannel = createPrivateChannel();
            channels.add(privateChannel);
            ChannelMemberFixture.create(user, privateChannel);
        }
        return channels;
    }
}
