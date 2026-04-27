package com.sprint.mission.discodeit.domain.channel.fixture;

import com.sprint.mission.discodeit.domain.channel.dto.query.ChannelInfoQuery;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import org.springframework.test.util.ReflectionTestUtils;

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
}
