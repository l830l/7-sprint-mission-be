package domain.message.fixture;

import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.user.entity.User;
import domain.binarycontent.fixture.BinaryContentFixture;
import domain.channel.fixture.ChannelFixture;
import domain.user.fixture.UserFixture;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageFixture {
    private static int messageCount = 1;

    // 기본 메세지 생성
    public static Message create(UUID channelId) {
        Message message = Message.create(
                ChannelFixture.createPublicChannel(),
                UserFixture.createWithoutProfile(),
                "메세지 내용" + messageCount,
                List.of(
                        BinaryContentFixture.create(),
                        BinaryContentFixture.create()
                )
        );
        ReflectionTestUtils.setField(message, "id", UUID.randomUUID());
        messageCount++;
        return message;
    }

    // 메세지 생성
    public static Message create(Channel channel, User speaker, List<BinaryContent> attachments) {
        Message message = Message.create(
                channel,
                speaker,
                "메세지 내용" + messageCount,
                attachments
        );
        ReflectionTestUtils.setField(message, "id", UUID.randomUUID());
        messageCount++;
        return message;
    }

    // 메세지 목록 생성
    public static List<Message> createList(UUID channelId) {
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            messages.add(create(channelId));
            messageCount++;
        }
        return messages;
    }
}